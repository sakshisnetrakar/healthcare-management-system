
import { useCallback, useEffect, useState } from "react";
import {
  Stethoscope,
  UserPlus,
  Search,
  Trash2,
  RefreshCw,
  X,
  AlertCircle,
  GraduationCap,
  Building2,
  BriefcaseBusiness,
} from "lucide-react";

import {
  getAllDoctors,
  searchDoctorsBySpecialization,
  getDoctorsByDepartment,
  addDoctor,
  deleteDoctor,
} from "../api/doctorService";

import { getAllDepartments } from "../api/departmentService";
import "./Doctors.css";

const emptyForm = {
  doctorName: "",
  specialization: "",
  qualification: "",
  experience: "",
  departmentId: "",
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

export default function Doctors() {
  const [doctors, setDoctors] = useState([]);
  const [departments, setDepartments] = useState([]);

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [searching, setSearching] = useState(false);

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(emptyForm);

  const [specialization, setSpecialization] = useState("");
  const [selectedDepartment, setSelectedDepartment] = useState("");
  const [isFiltered, setIsFiltered] = useState(false);

  const role = getUserRole();
  const canManage = role === "ADMIN";

  const loadDoctors = useCallback(async () => {
    setLoading(true);
    setError("");
    setIsFiltered(false);

    try {
      const response = await getAllDoctors();
      setDoctors(Array.isArray(response.data) ? response.data : []);
    } catch (err) {
      setDoctors([]);
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }, []);

  const loadDepartments = useCallback(async () => {
    try {
      const response = await getAllDepartments();
      setDepartments(Array.isArray(response.data) ? response.data : []);
    } catch (err) {
      setDepartments([]);
      setError(getErrorMessage(err));
    }
  }, []);

  useEffect(() => {
    loadDoctors();
    loadDepartments();
  }, [loadDoctors, loadDepartments]);

  function handleFormChange(event) {
    const { name, value } = event.target;

    setForm((current) => ({
      ...current,
      [name]: value,
    }));
  }

  async function handleSearch(event) {
    event.preventDefault();

    if (!specialization.trim()) {
      setError("Enter a specialization to search.");
      return;
    }

    setSearching(true);
    setError("");
    setSuccess("");
    setIsFiltered(true);

    try {
      const response = await searchDoctorsBySpecialization(
        specialization.trim()
      );

      setDoctors(Array.isArray(response.data) ? response.data : []);
      setSelectedDepartment("");
    } catch (err) {
      setDoctors([]);
      setError(getErrorMessage(err));
    } finally {
      setSearching(false);
    }
  }

  async function handleDepartmentFilter(event) {
    const departmentId = event.target.value;

    setSelectedDepartment(departmentId);
    setSpecialization("");
    setError("");
    setSuccess("");

    if (!departmentId) {
      await loadDoctors();
      return;
    }

    setLoading(true);
    setIsFiltered(true);

    try {
      const response = await getDoctorsByDepartment(departmentId);
      setDoctors(Array.isArray(response.data) ? response.data : []);
    } catch (err) {
      setDoctors([]);
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }

  async function handleAddDoctor(event) {
    event.preventDefault();
    setError("");
    setSuccess("");

    const experience = Number(form.experience);
    const departmentId = Number(form.departmentId);
    const userId = Number(form.userId);

    if (!form.doctorName.trim()) {
      setError("Doctor name is required.");
      return;
    }

    if (!form.specialization.trim()) {
      setError("Specialization is required.");
      return;
    }

    if (!form.qualification.trim()) {
      setError("Qualification is required.");
      return;
    }

    if (!Number.isInteger(experience) || experience < 0) {
      setError("Experience must be a non-negative whole number.");
      return;
    }

    if (!Number.isInteger(departmentId) || departmentId < 1) {
      setError("Please select a department.");
      return;
    }

    if (!Number.isInteger(userId) || userId < 1) {
      setError("Enter a valid User ID.");
      return;
    }

    const doctorData = {
      doctorName: form.doctorName.trim(),
      specialization: form.specialization.trim(),
      qualification: form.qualification.trim(),
      experience,
      departmentId,
      userId,
    };

    setSaving(true);

    try {
      await addDoctor(doctorData);

      setSuccess("Doctor added successfully.");
      setForm(emptyForm);
      setShowForm(false);

      await loadDoctors();
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSaving(false);
    }
  }

  async function handleDeleteDoctor(doctor) {
    const confirmed = window.confirm(
      `Are you sure you want to delete Dr. ${doctor.doctorName}?`
    );

    if (!confirmed) return;

    setError("");
    setSuccess("");

    try {
      await deleteDoctor(doctor.id);

      setDoctors((current) =>
        current.filter((item) => item.id !== doctor.id)
      );

      setSuccess("Doctor deleted successfully.");
    } catch (err) {
      setError(getErrorMessage(err));
    }
  }

  async function clearFilters() {
    setSpecialization("");
    setSelectedDepartment("");
    setSuccess("");
    await loadDoctors();
  }

  return (
    <main className="doctors-page">
      <header className="doctors-header">
        <div>
          <p className="doctors-eyebrow">DOCTOR MANAGEMENT</p>
          <h1>Doctors</h1>
          <p className="doctors-subtitle">
            Manage doctors, specializations and departments.
          </p>
        </div>

        {canManage && (
          <button
            type="button"
            className="doctors-primary-button"
            onClick={() => {
              setError("");
              setSuccess("");
              setShowForm((current) => !current);
            }}
          >
            {showForm ? <X size={18} /> : <UserPlus size={18} />}
            {showForm ? "Close form" : "Add doctor"}
          </button>
        )}
      </header>

      {error && (
        <div className="doctors-alert doctors-alert-error" role="alert">
          <AlertCircle size={18} />
          <span>{error}</span>
          <button type="button" onClick={() => setError("")}>
            <X size={17} />
          </button>
        </div>
      )}

      {success && (
        <div className="doctors-alert doctors-alert-success" role="status">
          <span>{success}</span>
          <button type="button" onClick={() => setSuccess("")}>
            <X size={17} />
          </button>
        </div>
      )}

      {showForm && canManage && (
        <section className="doctors-card doctors-form-card">
          <div className="doctors-section-heading">
            <div>
              <h2>Add New Doctor</h2>
              <p>Enter the doctor's information below.</p>
            </div>
          </div>

          <form onSubmit={handleAddDoctor}>
            <div className="doctors-form-grid">
              <label className="doctors-field">
                Doctor Name *
                <input
                  name="doctorName"
                  value={form.doctorName}
                  onChange={handleFormChange}
                  placeholder="Enter full name"
                  required
                />
              </label>

              <label className="doctors-field">
                Specialization *
                <input
                  name="specialization"
                  value={form.specialization}
                  onChange={handleFormChange}
                  placeholder="e.g. Cardiology"
                  required
                />
              </label>

              <label className="doctors-field">
                Qualification *
                <input
                  name="qualification"
                  value={form.qualification}
                  onChange={handleFormChange}
                  placeholder="e.g. MBBS, MD"
                  required
                />
              </label>

              <label className="doctors-field">
                Experience (years) *
                <input
                  name="experience"
                  type="number"
                  min="0"
                  step="1"
                  value={form.experience}
                  onChange={handleFormChange}
                  placeholder="Years of experience"
                  required
                />
              </label>

              <label className="doctors-field">
                Department *
                <select
                  name="departmentId"
                  value={form.departmentId}
                  onChange={handleFormChange}
                  required
                >
                  <option value="">Select department</option>
                  {departments.map((department) => (
                    <option key={department.id} value={department.id}>
                      {department.departmentName}
                    </option>
                  ))}
                </select>
                {departments.length === 0 && (
                  <small>No departments loaded. Check the API or add a department first.</small>
                )}
              </label>

              <label className="doctors-field">
                User ID *
                <input
                  name="userId"
                  type="number"
                  min="1"
                  step="1"
                  value={form.userId}
                  onChange={handleFormChange}
                  placeholder="Enter linked user ID"
                  required
                />
              </label>
            </div>

            <div className="doctors-form-actions">
              <button
                type="button"
                className="doctors-secondary-button"
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
                className="doctors-primary-button"
                disabled={saving}
              >
                {saving ? "Saving..." : "Save doctor"}
              </button>
            </div>
          </form>
        </section>
      )}

      <section className="doctors-card">
        <div className="doctors-toolbar">
          <div className="doctors-section-heading">
            <div className="doctors-icon-box">
              <Stethoscope size={22} />
            </div>

            <div>
              <h2>Doctor Directory</h2>
              <p>
                {loading
                  ? "Loading records..."
                  : `${doctors.length} doctor(s) shown`}
              </p>
            </div>
          </div>

          <div className="doctors-filters">
            <form className="doctors-search" onSubmit={handleSearch}>
              <Search size={17} />
              <input
                value={specialization}
                onChange={(event) =>
                  setSpecialization(event.target.value)
                }
                placeholder="Search specialization"
                aria-label="Search specialization"
              />
              <button type="submit" disabled={searching}>
                {searching ? "Searching..." : "Search"}
              </button>
            </form>

            <select
              className="doctors-department-filter"
              value={selectedDepartment}
              onChange={handleDepartmentFilter}
              aria-label="Filter by department"
            >
              <option value="">All departments</option>
              {departments.map((department) => (
                <option key={department.id} value={department.id}>
                  {department.departmentName}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className="doctors-table-wrapper">
          <table className="doctors-table">
            <thead>
              <tr>
                <th>Doctor</th>
                <th>Specialization</th>
                <th>Qualification</th>
                <th>Experience</th>
                <th>Department</th>
                <th>User ID</th>
                {canManage && <th>Action</th>}
              </tr>
            </thead>

            <tbody>
              {loading ? (
                <tr>
                  <td
                    colSpan={canManage ? 7 : 6}
                    className="doctors-empty"
                  >
                    <RefreshCw className="doctors-loading-icon" size={22} />
                    <p>Loading doctors...</p>
                  </td>
                </tr>
              ) : doctors.length === 0 ? (
                <tr>
                  <td
                    colSpan={canManage ? 7 : 6}
                    className="doctors-empty"
                  >
                    <Stethoscope size={28} />
                    <p>
                      {error
                        ? "Unable to load doctor records."
                        : isFiltered
                          ? "No doctors found for the selected filter."
                          : "No doctor records found."}
                    </p>

                    {isFiltered && (
                      <button
                        type="button"
                        className="doctors-text-button"
                        onClick={clearFilters}
                      >
                        View all doctors
                      </button>
                    )}
                  </td>
                </tr>
              ) : (
                doctors.map((doctor) => (
                  <tr key={doctor.id}>
                    <td>
                      <div className="doctors-name-cell">
                        <div className="doctors-avatar">
                          {(doctor.doctorName || "D")
                            .trim()
                            .charAt(0)
                            .toUpperCase()}
                        </div>
                        <div>
                          <strong>Dr. {doctor.doctorName}</strong>
                          <span>ID: {doctor.id}</span>
                        </div>
                      </div>
                    </td>

                    <td>
                      <span className="doctors-specialization">
                        <Stethoscope size={14} />
                        {doctor.specialization || "—"}
                      </span>
                    </td>

                    <td>
                      <span className="doctors-qualification">
                        <GraduationCap size={15} />
                        {doctor.qualification || "—"}
                      </span>
                    </td>

                    <td>
                      <span className="doctors-experience">
                        <BriefcaseBusiness size={14} />
                        {doctor.experience} years
                      </span>
                    </td>

                    <td>
                      <span className="doctors-department">
                        <Building2 size={14} />
                        {doctor.departmentName || `Department #${doctor.departmentId}`}
                      </span>
                    </td>

                    <td>{doctor.userId ?? "—"}</td>

                    {canManage && (
                      <td>
                        <button
                          type="button"
                          className="doctors-delete-button"
                          title="Delete doctor"
                          aria-label={`Delete ${doctor.doctorName}`}
                          onClick={() => handleDeleteDoctor(doctor)}
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

        <div className="doctors-table-footer">
          <span>
            {isFiltered ? "Filtered results" : "Doctor directory"}
          </span>

          <div>
            {isFiltered && (
              <button
                type="button"
                className="doctors-text-button"
                onClick={clearFilters}
              >
                Clear filters
              </button>
            )}

            <button
              type="button"
              className="doctors-refresh-button"
              onClick={() => {
                setSuccess("");
                loadDoctors();
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
