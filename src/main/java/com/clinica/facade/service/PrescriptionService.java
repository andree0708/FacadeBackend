package com.clinica.facade.service;

import com.clinica.facade.entity.Prescripcion;
import com.clinica.facade.entity.Medicamento;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {
    private final Map<String, Prescripcion> prescriptions = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public Prescripcion generate(String patientId, String doctorId, String doctorName, 
                               List<Medicamento> medications) {
        String id = "PRES-" + idCounter.getAndIncrement();
        Prescripcion prescription = new Prescripcion(id, patientId, doctorId, doctorName,
                                                     LocalDateTime.now(), medications);
        prescriptions.put(id, prescription);
        return prescription;
    }

    public List<Prescripcion> getPatientPrescriptions(String patientId) {
        return prescriptions.values().stream()
            .filter(p -> p.getPatientId().equals(patientId))
            .sorted(Comparator.comparing(Prescripcion::getDate).reversed())
            .collect(Collectors.toList());
    }

    public List<Prescripcion> getRecentPrescriptions(String patientId) {
        return prescriptions.values().stream()
            .filter(p -> p.getPatientId().equals(patientId))
            .sorted(Comparator.comparing(Prescripcion::getDate).reversed())
            .limit(3)
            .collect(Collectors.toList());
    }

    public boolean hasAllergy(String patientId, String medicationName) {
        return false;
    }
}