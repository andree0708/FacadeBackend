package com.clinica.facade.service;

import com.clinica.facade.data.DataStore;
import com.clinica.facade.entity.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicalHistoryService {
    
    @Autowired
    private DataStore dataStore;

    @SuppressWarnings("unchecked")
    public Consulta registerConsultation(String patientId, String doctorId, String doctorName, 
                                     String specialty, String diagnosis, String reason) {
        int id = dataStore.getNextId("consultation");
        String idStr = "CONS-" + id;
        
        Consulta consultation = new Consulta(idStr, patientId, doctorId, doctorName, specialty,
                                        LocalDateTime.now(), diagnosis, reason);
        
        Map<String, Object> consultations = dataStore.getStorage("consultations");
        consultations.put(idStr, consultation);
        dataStore.persist();
        
        return consultation;
    }

    @SuppressWarnings("unchecked")
    public List<Consulta> getPatientConsultations(String patientId) {
        Map<String, Object> consultations = dataStore.getStorage("consultations");
        return consultations.values().stream()
            .map(o -> (Consulta) o)
            .filter(c -> c.getPatientId().equals(patientId))
            .sorted(Comparator.comparing(Consulta::getDate).reversed())
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public String getPatientAllergies(String patientId) {
        Map<String, Object> consultations = dataStore.getStorage("consultations");
        return consultations.values().stream()
            .map(o -> (Consulta) o)
            .filter(c -> c.getPatientId().equals(patientId))
            .map(c -> c.getDiagnosis())
            .findFirst()
            .orElse("Not registered");
    }
}