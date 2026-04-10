package com.clinica.facade.service;

import com.clinica.facade.entity.Consulta;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MedicalHistoryService {
    private final Map<String, Consulta> consultations = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public Consulta registerConsultation(String patientId, String doctorId, String doctorName, 
                                     String specialty, String diagnosis, String reason) {
        String id = "CONS-" + idCounter.getAndIncrement();
        Consulta consultation = new Consulta(id, patientId, doctorId, doctorName, specialty,
                                        LocalDateTime.now(), diagnosis, reason);
        consultations.put(id, consultation);
        return consultation;
    }

    public List<Consulta> getPatientConsultations(String patientId) {
        return consultations.values().stream()
            .filter(c -> c.getPatientId().equals(patientId))
            .sorted(Comparator.comparing(Consulta::getDate).reversed())
            .collect(Collectors.toList());
    }

    public String getPatientAllergies(String patientId) {
        return consultations.values().stream()
            .filter(c -> c.getPatientId().equals(patientId))
            .map(c -> c.getDiagnosis())
            .findFirst()
            .orElse("Not registered");
    }
}