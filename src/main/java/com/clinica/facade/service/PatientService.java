package com.clinica.facade.service;

import com.clinica.facade.dto.PatientRequest;
import com.clinica.facade.entity.Paciente;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PatientService {
    private final Map<String, Paciente> patients = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public Paciente register(PatientRequest request) {
        for (Paciente p : patients.values()) {
            if (p.getDocument().equals(request.getDocument())) {
                throw new RuntimeException("Document already registered");
            }
        }
        String id = String.valueOf(idCounter.getAndIncrement());
        Paciente patient = new Paciente(
            id,
            request.getName(),
            request.getDocument(),
            request.getEmail(),
            request.getPhone(),
            request.getBirthDate(),
            request.getAllergies()
        );
        patients.put(id, patient);
        return patient;
    }

    public Optional<Paciente> findById(String id) {
        return Optional.ofNullable(patients.get(id));
    }

    public Optional<Paciente> findByDocument(String document) {
        return patients.values().stream()
            .filter(p -> p.getDocument().equals(document))
            .findFirst();
    }

    public List<Paciente> findAll() {
        return new ArrayList<>(patients.values());
    }

    public boolean existsById(String id) {
        return patients.containsKey(id);
    }
}