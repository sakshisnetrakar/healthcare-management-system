package com.healthcare.backend.service.impl;

import com.healthcare.backend.dto.request.DoctorRequestDTO;
import com.healthcare.backend.dto.response.DoctorResponseDTO;
import com.healthcare.backend.entity.Department;
import com.healthcare.backend.entity.Doctor;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.mapper.DoctorMapper;
import com.healthcare.backend.repository.DepartmentRepository;
import com.healthcare.backend.repository.DoctorRepository;
import com.healthcare.backend.repository.UserRepository;
import com.healthcare.backend.service.DoctorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    @Override
    public DoctorResponseDTO addDoctor(
            DoctorRequestDTO dto) {

        Doctor doctor =
                DoctorMapper.toEntity(dto);


        // Find User
        User user = userRepository
                .findById(dto.getUserId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));


        // Find Department
        Department department =
                departmentRepository
                        .findById(dto.getDepartmentId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Department not found"));


        doctor.setUser(user);
        doctor.setDepartment(department);


        Doctor savedDoctor =
                doctorRepository.save(doctor);

        return DoctorMapper.toResponseDTO(
                savedDoctor);
    }


    @Override
    public List<DoctorResponseDTO> getAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(DoctorMapper::toResponseDTO)
                .toList();
    }


    @Override
    public DoctorResponseDTO getDoctorById(Long id) {

        Doctor doctor =
                doctorRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Doctor not found"));

        return DoctorMapper.toResponseDTO(doctor);
    }


    @Override
    public void deleteDoctor(Long id) {

        if (!doctorRepository.existsById(id)) {

            throw new RuntimeException(
                    "Doctor not found");
        }

        doctorRepository.deleteById(id);
    }
}