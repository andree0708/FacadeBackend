package com.clinica.facade.entity;

import java.time.LocalDateTime;

public class ResultadoLaboratorio {
    private String id;
    private String patientId;
    private String testType;
    private LocalDateTime date;
    private double value;
    private String unit;
    private double minValue;
    private double maxValue;
    private boolean inRange;

    public ResultadoLaboratorio() {}

    public ResultadoLaboratorio(String id, String patientId, String testType, LocalDateTime date,
                               double value, String unit, double minValue, double maxValue) {
        this.id = id;
        this.patientId = patientId;
        this.testType = testType;
        this.date = date;
        this.value = value;
        this.unit = unit;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.inRange = value >= minValue && value <= maxValue;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; this.inRange = value >= minValue && value <= maxValue; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public double getMinValue() { return minValue; }
    public void setMinValue(double minValue) { this.minValue = minValue; }
    public double getMaxValue() { return maxValue; }
    public void setMaxValue(double maxValue) { this.maxValue = maxValue; }
    public boolean isInRange() { return inRange; }
    public void setInRange(boolean inRange) { this.inRange = inRange; }
}