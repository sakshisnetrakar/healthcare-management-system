
import { useCallback, useEffect, useState } from "react";
import {
  Users,
  UserPlus,
  Search,
  Trash2,
  RefreshCw,
  X,
  AlertCircle,
  Phone,
  MapPin,
  Droplets,
} from "lucide-react";

import {
  getAllPatients,
  searchPatientByPhone,
  addPatient,
  deletePatient,
} from "../api/patientService";

import "./Patients.css";

const emptyForm = {
  patientName: "",
  gender: "",
  age: "",
  bloodGroup: "",
  phoneNumber: "",
  address: "",
  userId: "",
};

function getUserRole() {
  try {
    const token = sessionStorage.getItem("token");
    if (!token) return "";

    const payload = JSON.parse(
      atob(token.split(".")[1].replace(/-/g, "+").replace(/_/g, "/"))
    );

    const role =
      payload.role ??
      payload.roles?.[0] ??
      payload.authorities?.[0];

    return String(role ?? "")
      .replace(/^ROLE_/, "")
      .toUpperCase();
  } catch {
    return "";
  }
}

function getErrorMessage(error) {
  const data = error?.response?.data;

  if (typeof data === "string" && data.trim()) return data;
  if (data?.message) return data.message;
  if (data?.error) return data.error;

  if (data && typeof data === "object") {
    const messages = Object.values(data)
      .filter((value) => typeof value === "string")
      .join(", ");

    if (messages) return messages;
  }

  if (error?.response?.status === 401) {
    return "Your session has expired. Please log in again.";
  }

  if (error?.response?.status === 403) {
    return "You do not have permission to perform this action.";
  }

  return "Something went wrong. Please try again.";
}

export default function Patients() {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [searching, setSearching] = useState(false);

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [showForm, setShowForm] = useState(false);

  const [searchPhone, setSearchPhone] = useState("");
  const [isSearchActive, setIsSearchActive] = useState(false);
  const [form, setForm] = useState(emptyForm);

  const role = getUserRole();
  const canAdd = role === "ADMIN";
  const canDelete = role === "ADMIN";

  const loadPatients = useCallback(async () => {
    setLoading(true);
    setError("");
    setIsSearchActive(false);

    try {
      const response = await getAllPatients();
      setPatients(Array.isArray(response.data) ? response.data : []);
    } catch (err) {
      setPatients([]);
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadPatients();
  }, [loadPatients]);

  function handleFormChange(event) {
    const { name, value } = event.target;

    setForm((current) => ({
      ...current,
      [name]: value,
    }));
  }

  async function handleSearch(event) {
    event.preventDefault();

    const phone = searchPhone.trim();

    if (!/^[0-9]{10}$/.test(phone)) {
      setError("Enter a valid 10-digit phone number.");
      return;
    }

    setSearching(true);
    setError("");
    setSuccess("");
    setIsSearchActive(true);

    try {
      const response = await searchPatientByPhone(phone);
      setPatients(response.data ? [response.data] : []);
    } catch (err) {
      setPatients([]);
      setError(getErrorMessage(err));
    } finally {
      setSearching(false);
    }
  }

  async function handleAddPatient(event) {
    event.preventDefault();
    setError("");
    setSuccess("");

    const age = Number(form.age);
    const phone = form.phoneNumber.trim();
    const userId = form.userId.trim()
      ? Number(form.userId)
      : null;

    if (!form.patientName.trim()) {
      setError("Patient name is required.");
      return;
    }

    if (!form.gender) {
      setError("Please select a gender.");
      return;
    }

    if (!Number.isInteger(age) || age < 1 || age > 120) {
      setError("Age must be between 1 and 120.");
      return;
    }

    if (!form.bloodGroup.trim()) {
      setError("Blood group is required.");
      return;
    }

    if (!/^[0-9]{10}$/.test(phone)) {
      setError("Phone number must contain exactly 10 digits.");
      return;
    }

    if (!form.address.trim()) {
      setError("Address is required.");
      return;
    }

    if (
      userId !== null &&
      (!Number.isInteger(userId) || userId < 1)
    ) {
      setError("User ID must be a positive whole number.");
      return;
    }

    const patientData = {
      patientName: form.patientName.trim(),
      gender: form.gender,
      age,
      bloodGroup: form.bloodGroup.trim(),
      phoneNumber: phone,
      address: form.address.trim(),
      userId,
    };

    setSaving(true);

    try {
      await addPatient(patientData);

      setSuccess("Patient added successfully.");
      setForm(emptyForm);
      setShowForm(false);

      await loadPatients();
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(patient) {
    if (
      !window.confirm(
        `Are you sure you want to delete ${patient.patientName}?`
      )
    ) {
      return;
    }

    setError("");
    setSuccess("");

    try {
      await deletePatient(patient.id);

      setPatients((current) =>
        current.filter((item) => item.id !== patient.id)
      );

      setSuccess("Patient deleted successfully.");
    } catch (err) {
      setError(getErrorMessage(err));
    }
  }

  function clearSearch() {
    setSearchPhone("");
    setSuccess("");
    loadPatients();
  }

  return (
    <main className="patients-page">
      <header className="patients-header">
        <div>
          <p className="patients-eyebrow">PATIENT MANAGEMENT</p>
          <h1>Patients</h1>
          <p className="patients-subtitle">
            Manage and view patient records in one place.
          </p>
        </div>

        {canAdd && (
          <button
            type="button"
            className="patients-primary-button"
            onClick={() => {
              setError("");
              setSuccess("");
              setShowForm((current) => !current);
            }}
          >
            {showForm ? <X size={18} /> : <UserPlus size={18} />}
            {showForm ? "Close form" : "Add patient"}
          </button>
        )}
      </header>

      {error && (
        <div className="patients-alert patients-alert-error" role="alert">
          <AlertCircle size={18} />
          <span>{error}</span>
          <button type="button" onClick={() => setError("")}>
            <X size={17} />
          </button>
        </div>
      )}

      {success && (
        <div className="patients-alert patients-alert-success" role="status">
          <span>{success}</span>
          <button type="button" onClick={() => setSuccess("")}>
            <X size={17} />
          </button>
        </div>
      )}

      {showForm && canAdd && (
        <section className="patients-card patients-form-card">
          <div className="patients-section-heading">
            <div>
              <h2>Add New Patient</h2>
              <p>Enter the patient's information.</p>
            </div>
          </div>

          <form onSubmit={handleAddPatient}>
            <div className="patients-form-grid">
              <label className="patients-field">
                Patient Name *
                <input
                  name="patientName"
                  value={form.patientName}
                  onChange={handleFormChange}
                  placeholder="Enter full name"
                  required
                />
              </label>

              <label className="patients-field">
                Gender *
                <select
                  name="gender"
                  value={form.gender}
                  onChange={handleFormChange}
                  required
                >
                  <option value="">Select gender</option>
                  <option value="MALE">Male</option>
                  <option value="FEMALE">Female</option>
                  <option value="OTHER">Other</option>
                </select>
              </label>

              <label className="patients-field">
                Age *
                <input
                  name="age"
                  type="number"
                  min="1"
                  max="120"
                  step="1"
                  value={form.age}
                  onChange={handleFormChange}
                  placeholder="Enter age"
                  required
                />
              </label>

              <label className="patients-field">
                Blood Group *
                <select
                  name="bloodGroup"
                  value={form.bloodGroup}
                  onChange={handleFormChange}
                  required
                >
                  <option value="">Select blood group</option>
                  {["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"].map(
                    (group) => (
                      <option key={group} value={group}>
                        {group}
                      </option>
                    )
                  )}
                </select>
              </label>

              <label className="patients-field">
                Phone Number *
                <input
                  name="phoneNumber"
                  type="tel"
                  inputMode="numeric"
                  pattern="[0-9]{10}"
                  maxLength={10}
                  value={form.phoneNumber}
                  onChange={handleFormChange}
                  placeholder="10-digit phone number"
                  required
                />
              </label>

              <label className="patients-field">
                User ID
                <input
                  name="userId"
                  type="number"
                  min="1"
                  step="1"
                  value={form.userId}
                  onChange={handleFormChange}
                  placeholder="Optional linked user ID"
                />
              </label>

              <label className="patients-field patients-field-full">
                Address *
                <textarea
                  name="address"
                  value={form.address}
                  onChange={handleFormChange}
                  placeholder="Enter full address"
                  rows={3}
                  required
                />
              </label>
            </div>

            <div className="patients-form-actions">
              <button
                type="button"
                className="patients-secondary-button"
                disabled={saving}
                onClick={() => {
                  setShowForm(false);
                  setForm(emptyForm);
                  setError("");
                }}
              >
                Cancel
              </button>

              <button
                type="submit"
                className="patients-primary-button"
                disabled={saving}
              >
                {saving ? "Saving..." : "Save Patient"}
              </button>
            </div>
          </form>
        </section>
      )}

      <section className="patients-card">
        <div className="patients-toolbar">
          <div className="patients-section-heading">
            <div className="patients-icon-box">
              <Users size={21} />
            </div>
            <div>
              <h2>Patient Records</h2>
              <p>
                {loading
                  ? "Loading records..."
                  : `${patients.length} patient(s) shown`}
              </p>
            </div>
          </div>

          <form className="patients-search" onSubmit={handleSearch}>
            <Search size={18} />
            <input
              type="tel"
              inputMode="numeric"
              value={searchPhone}
              onChange={(event) => setSearchPhone(event.target.value)}
              placeholder="Search by phone"
              aria-label="Search by phone number"
            />
            <button type="submit" disabled={searching}>
              {searching ? "Searching..." : "Search"}
            </button>
          </form>
        </div>

        <div className="patients-table-wrapper">
          <table className="patients-table">
            <thead>
              <tr>
                <th>Patient</th>
                <th>Gender / Age</th>
                <th>Blood Group</th>
                <th>Contact</th>
                <th>Address</th>
                <th>User ID</th>
                {canDelete && <th>Action</th>}
              </tr>
            </thead>

            <tbody>
              {loading ? (
                <tr>
                  <td
                    colSpan={canDelete ? 7 : 6}
                    className="patients-empty"
                  >
                    <RefreshCw
                      className="patients-loading-icon"
                      size={22}
                    />
                    <p>Loading patients...</p>
                  </td>
                </tr>
              ) : patients.length === 0 ? (
                <tr>
                  <td
                    colSpan={canDelete ? 7 : 6}
                    className="patients-empty"
                  >
                    <Users size={28} />
                    <p>
                      {error
                        ? "Unable to load patient records."
                        : isSearchActive
                          ? "No patient found for that phone number."
                          : "No patient records found."}
                    </p>

                    {isSearchActive && (
                      <button
                        type="button"
                        className="patients-text-button"
                        onClick={clearSearch}
                      >
                        View all patients
                      </button>
                    )}
                  </td>
                </tr>
              ) : (
                patients.map((patient) => (
                  <tr key={patient.id}>
                    <td>
                      <div className="patients-name-cell">
                        <div className="patients-avatar">
                          {(patient.patientName || "P")
                            .trim()
                            .charAt(0)
                            .toUpperCase()}
                        </div>
                        <div>
                          <strong>{patient.patientName}</strong>
                          <span>ID: {patient.id}</span>
                        </div>
                      </div>
                    </td>

                    <td>
                      <span>{patient.gender || "—"}</span>
                      <span className="patients-muted">
                        {patient.age != null
                          ? `${patient.age} years`
                          : "—"}
                      </span>
                    </td>

                    <td>
                      <span className="patients-blood-group">
                        <Droplets size={14} />
                        {patient.bloodGroup || "—"}
                      </span>
                    </td>

                    <td>
                      <span className="patients-contact">
                        <Phone size={14} />
                        {patient.phoneNumber || "—"}
                      </span>
                    </td>

                    <td>
                      <span className="patients-address">
                        <MapPin size={14} />
                        {patient.address || "—"}
                      </span>
                    </td>

                    <td>{patient.userId ?? "—"}</td>

                    {canDelete && (
                      <td>
                        <button
                          type="button"
                          className="patients-delete-button"
                          title="Delete patient"
                          aria-label={`Delete ${patient.patientName}`}
                          onClick={() => handleDelete(patient)}
                        >
                          <Trash2 size={17} />
                        </button>
                      </td>
                    )}
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        <div className="patients-table-footer">
          <span>
            {isSearchActive ? "Search Results" : "Patient Directory"}
          </span>

          <div>
            {isSearchActive && (
              <button
                type="button"
                className="patients-text-button"
                onClick={clearSearch}
              >
                Clear Search
              </button>
            )}

            <button
              type="button"
              className="patients-refresh-button"
              onClick={() => {
                setSuccess("");
                loadPatients();
              }}
              disabled={loading}
            >
              <RefreshCw size={15} />
              Refresh
            </button>
          </div>
        </div>
      </section>
    </main>
  );
}