package com.clinica.facade.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Prescripcion {
    private String id;
    private String patientId;
    private String doctorId;
    private String doctorName;
    private LocalDateTime date;
    private List<Medicamento> medications;

    public Prescripcion() {}

    public Prescripcion(String id, String patientId, String doctorId, String doctorName, 
                        LocalDateTime date, List<Medicamento> medications) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date;
        this.medications = medications;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public List<Medicamento> getMedications() { return medications; }
    public void setMedications(List<Medicamento> medications) { this.medications = medications; }
}