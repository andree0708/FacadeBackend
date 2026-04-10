package com.clinica.facade.service;

import com.clinica.facade.data.DataStore;
import com.clinica.facade.dto.PatientRequest;
import com.clinica.facade.entity.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {
    
    @Autowired
    private DataStore dataStore;

    @SuppressWarnings("unchecked")
    public Paciente register(PatientRequest request) {
        Map<String, Object> patients = dataStore.getStorage("patients");
        
        for (Object obj : patients.values()) {
            Paciente p = (Paciente) obj;
            if (p.getDocument().equals(request.getDocument())) {
                throw new RuntimeException("Document already registered");
            }
        }
        
        int id = dataStore.getNextId("patient");
        String idStr = String.valueOf(id);
        
        Paciente patient = new Paciente(
            idStr,
            request.getName(),
            request.getDocument(),
            request.getEmail(),
            request.getPhone(),
            request.getBirthDate(),
            request.getAllergies()
        );
        
        patients.put(idStr, patient);
        dataStore.persist();
        
        return patient;
    }

    @SuppressWarnings("unchecked")
    public Optional<Paciente> findById(String id) {
        Map<String, Object> patients = dataStore.getStorage("patients");
        Paciente patient = (Paciente) patients.get(id);
        return Optional.ofNullable(patient);
    }

    @SuppressWarnings("unchecked")
    public Optional<Paciente> findByDocument(String document) {
        Map<String, Object> patients = dataStore.getStorage("patients");
        return patients.values().stream()
            .map(p -> (Paciente) p)
            .filter(p -> p.getDocument().equals(document))
            .findFirst();
    }

    @SuppressWarnings("unchecked")
    public List<Paciente> findAll() {
        Map<String, Object> patients = dataStore.getStorage("patients");
        return patients.values().stream()
            .map(p -> (Paciente) p)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public boolean existsById(String id) {
        Map<String, Object> patients = dataStore.getStorage("patients");
        return patients.containsKey(id);
    }
}