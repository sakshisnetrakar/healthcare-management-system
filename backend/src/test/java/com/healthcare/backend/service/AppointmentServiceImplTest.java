package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.AppointmentRequestDTO;
import com.healthcare.backend.dto.response.AppointmentResponseDTO;
import com.healthcare.backend.entity.Appointment;
import com.healthcare.backend.entity.Doctor;
import com.healthcare.backend.entity.Patient;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.enums.AppointmentStatus;
import com.healthcare.backend.enums.Gender;
import com.healthcare.backend.enums.Role;
import com.healthcare.backend.repository.AppointmentRepository;
import com.healthcare.backend.repository.DoctorRepository;
import com.healthcare.backend.repository.PatientRepository;
import com.healthcare.backend.repository.UserRepository;
import com.healthcare.backend.service.impl.AppointmentServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // =====================================================
    // HELPER METHOD
    // =====================================================

    private void setLoggedInUser(String email) {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }

    // =====================================================
    // TEST 1
    // SUCCESSFUL APPOINTMENT BOOKING
    // =====================================================

    @Test
    void bookAppointment_shouldBookSuccessfully() {

        setLoggedInUser("patient@gmail.com");

        // User
        User user = new User();
        user.setId(1L);
        user.setEmail("patient@gmail.com");
        user.setRole(Role.PATIENT);
        user.setActive(true);

        // Patient
        Patient patient = new Patient();

        ReflectionTestUtils.setField(
                patient,
                "id",
                1L
        );

        patient.setPatientName("Test Patient");
        patient.setGender(Gender.MALE);
        patient.setAge(25);
        patient.setBloodGroup("O+");
        patient.setPhoneNumber("9876543210");
        patient.setAddress("Bangalore");

        // Doctor
        Doctor doctor = new Doctor();

        ReflectionTestUtils.setField(
                doctor,
                "id",
                2L
        );

        doctor.setDoctorName("Dr. Rahul Sharma");
        doctor.setSpecialization("Cardiology");
        doctor.setQualification("MBBS");
        doctor.setExperience(5);

        // Request
        AppointmentRequestDTO request =
                new AppointmentRequestDTO();

        request.setAppointmentDate(
                LocalDate.now().plusDays(5)
        );

        request.setAppointmentTime(
                LocalTime.of(10, 30)
        );

        request.setDoctorId(2L);

        // Mock repository calls
        when(userRepository.findByEmail("patient@gmail.com"))
                .thenReturn(Optional.of(user));

        when(patientRepository.findByUserId(1L))
                .thenReturn(Optional.of(patient));

        when(doctorRepository.findById(2L))
                .thenReturn(Optional.of(doctor));

        when(
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                                2L,
                                request.getAppointmentDate(),
                                request.getAppointmentTime()
                        )
        ).thenReturn(false);

        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        // Execute
        AppointmentResponseDTO response =
                appointmentService.bookAppointment(request);

        // Verify response
        assertNotNull(response);

        assertEquals(
                request.getAppointmentDate(),
                response.getAppointmentDate()
        );

        assertEquals(
                request.getAppointmentTime(),
                response.getAppointmentTime()
        );

        assertEquals(
                AppointmentStatus.BOOKED,
                response.getStatus()
        );

        assertEquals(
                2L,
                response.getDoctorId()
        );

        assertEquals(
                1L,
                response.getPatientId()
        );

        // Verify save was called
        verify(appointmentRepository)
                .save(any(Appointment.class));
    }

    // =====================================================
    // TEST 2
    // DUPLICATE APPOINTMENT
    // =====================================================

    @Test
    void bookAppointment_shouldRejectDuplicateAppointment() {

        setLoggedInUser("patient@gmail.com");

        // User
        User user = new User();

        user.setId(1L);
        user.setEmail("patient@gmail.com");
        user.setRole(Role.PATIENT);
        user.setActive(true);

        // Patient
        Patient patient = new Patient();

        ReflectionTestUtils.setField(
                patient,
                "id",
                1L
        );

        patient.setPatientName("Test Patient");

        // Doctor
        Doctor doctor = new Doctor();

        ReflectionTestUtils.setField(
                doctor,
                "id",
                2L
        );

        doctor.setDoctorName("Dr. Rahul Sharma");

        // Request
        AppointmentRequestDTO request =
                new AppointmentRequestDTO();

        request.setAppointmentDate(
                LocalDate.now().plusDays(5)
        );

        request.setAppointmentTime(
                LocalTime.of(10, 30)
        );

        request.setDoctorId(2L);

        // Mock repository calls
        when(userRepository.findByEmail("patient@gmail.com"))
                .thenReturn(Optional.of(user));

        when(patientRepository.findByUserId(1L))
                .thenReturn(Optional.of(patient));

        when(doctorRepository.findById(2L))
                .thenReturn(Optional.of(doctor));

        // Appointment already exists
        when(
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                                2L,
                                request.getAppointmentDate(),
                                request.getAppointmentTime()
                        )
        ).thenReturn(true);

        // Execute and expect exception
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                appointmentService
                                        .bookAppointment(request)
                );

        // Verify message
        assertEquals(
                "Doctor is already booked for this date and time",
                exception.getMessage()
        );

        // Save should NOT happen
        verify(
                appointmentRepository,
                never()
        ).save(any(Appointment.class));
    }

    // =====================================================
    // TEST 3
    // INACTIVE USER
    // =====================================================

    @Test
    void bookAppointment_shouldRejectInactiveUser() {

        setLoggedInUser("inactive@gmail.com");

        // Inactive user
        User user = new User();

        user.setId(5L);
        user.setEmail("inactive@gmail.com");
        user.setRole(Role.PATIENT);
        user.setActive(false);

        // Request
        AppointmentRequestDTO request =
                new AppointmentRequestDTO();

        request.setAppointmentDate(
                LocalDate.now().plusDays(5)
        );

        request.setAppointmentTime(
                LocalTime.of(10, 30)
        );

        request.setDoctorId(2L);

        when(
                userRepository.findByEmail("inactive@gmail.com")
        ).thenReturn(Optional.of(user));

        // Execute
        AccessDeniedException exception =
                assertThrows(
                        AccessDeniedException.class,
                        () ->
                                appointmentService
                                        .bookAppointment(request)
                );

        // Verify message
        assertEquals(
                "Inactive users cannot book appointments",
                exception.getMessage()
        );

        // Save should NOT happen
        verify(
                appointmentRepository,
                never()
        ).save(any(Appointment.class));
    }

    // =====================================================
    // TEST 4
    // WRONG PATIENT ACCESS
    // =====================================================

    @Test
    void getAppointmentById_shouldRejectWrongPatient() {

        setLoggedInUser("patient2@gmail.com");

        // Logged-in user
        User user = new User();

        user.setId(22L);
        user.setEmail("patient2@gmail.com");
        user.setRole(Role.PATIENT);
        user.setActive(true);

        // Logged-in patient
        Patient loggedInPatient =
                new Patient();

        ReflectionTestUtils.setField(
                loggedInPatient,
                "id",
                3L
        );

        loggedInPatient.setPatientName(
                "Patient Two"
        );

        // Appointment belongs to another patient
        Patient appointmentPatient =
                new Patient();

        ReflectionTestUtils.setField(
                appointmentPatient,
                "id",
                1L
        );

        appointmentPatient.setPatientName(
                "Patient One"
        );

        // Doctor
        Doctor doctor =
                new Doctor();

        ReflectionTestUtils.setField(
                doctor,
                "id",
                2L
        );

        doctor.setDoctorName(
                "Dr. Rahul Sharma"
        );

        // Appointment
        Appointment appointment =
                new Appointment();

        ReflectionTestUtils.setField(
                appointment,
                "id",
                10L
        );

        appointment.setAppointmentDate(
                LocalDate.now().plusDays(5)
        );

        appointment.setAppointmentTime(
                LocalTime.of(10, 30)
        );

        appointment.setStatus(
                AppointmentStatus.BOOKED
        );

        appointment.setPatient(
                appointmentPatient
        );

        appointment.setDoctor(
                doctor
        );

        // Mock repository
        when(
                appointmentRepository.findById(10L)
        ).thenReturn(Optional.of(appointment));

        when(
                userRepository.findByEmail(
                        "patient2@gmail.com"
                )
        ).thenReturn(Optional.of(user));

        when(
                patientRepository.findByUserId(22L)
        ).thenReturn(Optional.of(loggedInPatient));

        // Execute
        AccessDeniedException exception =
                assertThrows(
                        AccessDeniedException.class,
                        () ->
                                appointmentService
                                        .getAppointmentById(10L)
                );

        // Verify message
        assertEquals(
                "You can access only your own appointments",
                exception.getMessage()
        );
    }
}