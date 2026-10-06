
import api from "./axios";

export const getAllDepartments = () =>
  api.get("/departments");

export const getDepartmentById = (id) =>
  api.get(`/departments/${id}`);

export const addDepartment = (departmentData) =>
  api.post("/departments", departmentData);

export const deleteDepartment = (id) =>
  api.delete(`/departments/${id}`);
