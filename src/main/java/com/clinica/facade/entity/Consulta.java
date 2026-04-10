package com.clinica.facade.entity;

import java.time.LocalDateTime;

public class Consulta {
    private String id;
    private String patientId;
    private String doctorId;
    private String doctorName;
    private String specialty;
    private LocalDateTime date;
    private String diagnosis;
    private String reason;

    public Consulta() {}

    public Consulta(String id, String patientId, String doctorId, String doctorName, String specialty,
                    LocalDateTime date, String diagnosis, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialty = specialty;
        this.date = date;
        this.diagnosis = diagnosis;
        this.reason = reason;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}