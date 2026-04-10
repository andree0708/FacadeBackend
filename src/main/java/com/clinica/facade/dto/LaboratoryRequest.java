package com.clinica.facade.dto;

import java.util.List;

public class LaboratoryRequest {
    private String patientId;
    private List<String> tests;

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public List<String> getTests() { return tests; }
    public void setTests(List<String> tests) { this.tests = tests; }
}