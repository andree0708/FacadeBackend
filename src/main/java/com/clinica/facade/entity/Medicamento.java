package com.clinica.facade.entity;

public class Medicamento {
    private String name;
    private String dose;
    private String duration;

    public Medicamento() {}

    public Medicamento(String name, String dose, String duration) {
        this.name = name;
        this.dose = dose;
        this.duration = duration;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}