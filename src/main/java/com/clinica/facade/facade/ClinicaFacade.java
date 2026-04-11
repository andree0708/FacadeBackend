package com.clinica.facade.facade;

import com.clinica.facade.dto.*;
import com.clinica.facade.entity.*;
import com.clinica.facade.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ClinicaFacade {

    @Autowired private PatientService patientService;
    @Autowired private AppointmentService appointmentService;
    @Autowired private MedicalHistoryService medicalHistoryService;
    @Autowired private PrescriptionService prescriptionService;
    @Autowired private LaboratoryService laboratoryService;

    public Paciente registerPatient(PatientRequest request) {
        return patientService.register(request);
    }

    public Optional<Paciente> findPatient(String id) {
        return patientService.findById(id);
    }

    public List<Paciente> getAllPatients() {
        return patientService.findAll();
    }

    public Cita scheduleAppointment(String patientId, String specialty, LocalDateTime dateTime, 
                           String doctorId) {
        Optional<Paciente> patientOpt = patientService.findById(patientId);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found");
        }
        Paciente patient = patientOpt.get();
        List<Medico> doctors = appointmentService.getDoctorsBySpecialty(specialty);
        Medico doctor = doctors.stream()
            .filter(m -> m.getId().equals(doctorId))
            .findFirst()
            .orElse(doctors.get(0));
        
        Cita appointment = appointmentService.schedule(patientId, patient.getName(), doctor.getId(),
                                          doctor.getName(), specialty, dateTime);
        
        medicalHistoryService.registerConsultation(patientId, doctor.getId(), doctor.getName(),
                                                  specialty, "Cita agendada", "Agenda");
        
        return appointment;
    }

    public boolean cancelAppointment(String appointmentId) {
        return appointmentService.cancel(appointmentId);
    }

    public CompleteHistory viewCompleteHistory(String patientId) {
        Optional<Paciente> patientOpt = patientService.findById(patientId);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found");
        }
        Paciente patient = patientOpt.get();
        
        List<Cita> pastAppointments = appointmentService.getPatientAppointments(patientId);
        List<Prescripcion> prescriptions = prescriptionService.getPatientPrescriptions(patientId);
        List<ResultadoLaboratorio> results = laboratoryService.getPatientResults(patientId);
        List<Consulta> consultations = medicalHistoryService.getPatientConsultations(patientId);
        
        return new CompleteHistory(patientId, patient.getName(), pastAppointments, 
                                    prescriptions, results, consultations);
    }

    public Prescripcion generatePrescription(String patientId, List<Medicamento> medications) {
        Optional<Paciente> patientOpt = patientService.findById(patientId);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found");
        }
        
        Paciente patient = patientOpt.get();
        for (Medicamento med : medications) {
            if (patient.getAllergies() != null && 
                patient.getAllergies().toLowerCase().contains(med.getName().toLowerCase())) {
                throw new RuntimeException("Patient is allergic to medication: " + med.getName());
            }
        }
        
        String doctorId = "1";
        String doctorName = "Dr. Carlos Gomez";
        
        Prescripcion prescription = prescriptionService.generate(patientId, doctorId, doctorName, medications);
        medicalHistoryService.registerConsultation(patientId, doctorId, doctorName, "General Medicine",
                                                  "Prescripción generada", "Tratamiento");
        
        return prescription;
    }

    public List<ResultadoLaboratorio> requestExams(String patientId, List<String> tests) {
        Optional<Paciente> patientOpt = patientService.findById(patientId);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found");
        }
        
        List<ResultadoLaboratorio> results = laboratoryService.request(patientId, tests);
        medicalHistoryService.registerConsultation(patientId, "LAB", "Laboratory", "Laboratory",
                                                  "Exámenes solicitados: " + String.join(", ", tests), "Laboratorio");
        
        return results;
    }

    public List<Medico> getDoctorsBySpecialty(String specialty) {
        return appointmentService.getDoctorsBySpecialty(specialty);
    }

    public List<Medico> getAllDoctors() {
        return appointmentService.getAllDoctors();
    }

    public List<java.time.LocalTime> getAvailableSchedules(String doctorId, LocalDateTime date) {
        return appointmentService.getAvailableSchedules(doctorId, date);
    }

    public List<Cita> getUpcomingAppointments(String patientId) {
        return appointmentService.getUpcomingAppointments(patientId);
    }

    public List<ResultadoLaboratorio> getRecentResults(String patientId) {
        return laboratoryService.getRecentResults(patientId);
    }

    public List<Prescripcion> getRecentPrescriptions(String patientId) {
        return prescriptionService.getRecentPrescriptions(patientId);
    }
}