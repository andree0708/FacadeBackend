package com.clinica.facade.controller;

import com.clinica.facade.dto.*;
import com.clinica.facade.entity.*;
import com.clinica.facade.facade.ClinicaFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clinica")
@CrossOrigin(origins = "*")
public class ClinicaController {
    
    private static final Logger logger = LoggerFactory.getLogger(ClinicaController.class);

    @Autowired
    private ClinicaFacade clinicaFacade;

    @PostMapping("/paciente")
    public ResponseEntity<?> registerPatient(@RequestBody PatientRequest request) {
        try {
            logger.info("Registering patient: {}", request.getName());
            Paciente patient = clinicaFacade.registerPatient(request);
            logger.info("Patient registered successfully with ID: {}", patient.getId());
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            logger.error("Error registering patient: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/cita")
    public ResponseEntity<Cita> scheduleAppointment(@RequestBody AppointmentRequest request) {
        try {
            Cita appointment = clinicaFacade.scheduleAppointment(
                request.getPatientId(),
                request.getSpecialty(),
                request.getDateTime(),
                request.getDoctorId()
            );
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            logger.error("Error scheduling appointment: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/historia/{patientId}")
    public ResponseEntity<CompleteHistory> getCompleteHistory(@PathVariable String patientId) {
        try {
            CompleteHistory history = clinicaFacade.viewCompleteHistory(patientId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            logger.error("Error getting history: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/prescripcion")
    public ResponseEntity<Prescripcion> generatePrescription(@RequestBody PrescriptionRequest request) {
        try {
            Prescripcion prescription = clinicaFacade.generatePrescription(
                request.getPatientId(),
                request.getMedications()
            );
            return ResponseEntity.ok(prescription);
        } catch (Exception e) {
            logger.error("Error generating prescription: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/laboratorio")
    public ResponseEntity<List<ResultadoLaboratorio>> requestExams(@RequestBody LaboratoryRequest request) {
        try {
            List<ResultadoLaboratorio> results = clinicaFacade.requestExams(
                request.getPatientId(),
                request.getTests()
            );
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error requesting labs: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/medicos")
    public ResponseEntity<List<Medico>> getDoctors(@RequestParam(required = false) String specialty) {
        List<Medico> doctors;
        if (specialty != null && !specialty.isEmpty()) {
            doctors = clinicaFacade.getDoctorsBySpecialty(specialty);
        } else {
            doctors = clinicaFacade.getAllDoctors();
        }
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/horarios/{doctorId}")
    public ResponseEntity<List<java.time.LocalTime>> getAvailableSchedules(
            @PathVariable String doctorId,
            @RequestParam String date) {
        try {
            java.time.LocalDateTime dateTime = java.time.LocalDateTime.parse(date);
            List<java.time.LocalTime> schedules = clinicaFacade.getAvailableSchedules(doctorId, dateTime);
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/citas/proximas/{patientId}")
    public ResponseEntity<List<Cita>> getUpcomingAppointments(@PathVariable String patientId) {
        List<Cita> appointments = clinicaFacade.getUpcomingAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/laboratorio/recientes/{patientId}")
    public ResponseEntity<List<ResultadoLaboratorio>> getRecentResults(@PathVariable String patientId) {
        List<ResultadoLaboratorio> results = clinicaFacade.getRecentResults(patientId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/paciente")
    public ResponseEntity<List<Paciente>> getAllPatients() {
        List<Paciente> patients = clinicaFacade.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/prescripciones/recientes/{patientId}")
    public ResponseEntity<List<Prescripcion>> getRecentPrescriptions(@PathVariable String patientId) {
        List<Prescripcion> prescriptions = clinicaFacade.getRecentPrescriptions(patientId);
        return ResponseEntity.ok(prescriptions);
    }
    
    @DeleteMapping("/cita/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable String appointmentId) {
        try {
            clinicaFacade.cancelAppointment(appointmentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}