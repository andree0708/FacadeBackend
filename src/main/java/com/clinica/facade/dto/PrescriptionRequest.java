package com.clinica.facade.dto;

import com.clinica.facade.entity.Medicamento;
import java.util.List;

public class PrescriptionRequest {
    private String patientId;
    private String doctorId;
    private List<Medicamento> medications;

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public List<Medicamento> getMedications() { return medications; }
    public void setMedications(List<Medicamento> medications) { this.medications = medications; }
}