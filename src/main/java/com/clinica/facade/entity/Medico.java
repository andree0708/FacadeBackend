package com.clinica.facade.entity;

import java.time.LocalDateTime;

public class Medico {
    private String id;
    private String name;
    private String specialty;

    public Medico() {}

    public Medico(String id, String name, String specialty) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}