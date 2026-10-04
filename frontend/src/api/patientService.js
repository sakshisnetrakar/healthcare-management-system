
import api from "./axios";

export const getAllPatients = () => api.get("/patients");

export const getPatientById = (id) =>
  api.get(`/patients/${id}`);

export const searchPatientByPhone = (phoneNumber) =>
  api.get("/patients/search", {
    params: { phoneNumber },
  });

export const addPatient = (patientData) =>
  api.post("/patients", patientData);

export const deletePatient = (id) =>
  api.delete(`/patients/${id}`);
