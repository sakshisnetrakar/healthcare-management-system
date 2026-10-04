import api from "./axios";

export const getAllDoctors = () => api.get("/doctors");

export const getDoctorById = (id) => api.get(`/doctors/${id}`);

export const searchDoctorsBySpecialization = (specialization) =>
  api.get("/doctors/search", {
    params: { specialization },
  });

export const getDoctorsByDepartment = (departmentId) =>
  api.get(`/doctors/department/${departmentId}`);

export const addDoctor = (doctorData) =>
  api.post("/doctors", doctorData);

export const deleteDoctor = (id) =>
  api.delete(`/doctors/${id}`);