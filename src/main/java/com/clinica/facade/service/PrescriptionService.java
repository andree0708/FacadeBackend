package com.clinica.facade.service;

import com.clinica.facade.data.DataStore;
import com.clinica.facade.entity.Prescripcion;
import com.clinica.facade.entity.Medicamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {
    
    @Autowired
    private DataStore dataStore;

    @SuppressWarnings("unchecked")
    public Prescripcion generate(String patientId, String doctorId, String doctorName, 
                               List<Medicamento> medications) {
        int id = dataStore.getNextId("prescription");
        String idStr = "PRES-" + id;
        
        Prescripcion prescription = new Prescripcion(idStr, patientId, doctorId, doctorName,
                                                     LocalDateTime.now(), medications);
        
        Map<String, Object> prescriptions = dataStore.getStorage("prescriptions");
        prescriptions.put(idStr, prescription);
        dataStore.persist();
        
        return prescription;
    }

    @SuppressWarnings("unchecked")
    public List<Prescripcion> getPatientPrescriptions(String patientId) {
        Map<String, Object> prescriptions = dataStore.getStorage("prescriptions");
        return prescriptions.values().stream()
            .map(o -> (Prescripcion) o)
            .filter(p -> p.getPatientId().equals(patientId))
            .sorted(Comparator.comparing(Prescripcion::getDate).reversed())
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<Prescripcion> getRecentPrescriptions(String patientId) {
        Map<String, Object> prescriptions = dataStore.getStorage("prescriptions");
        return prescriptions.values().stream()
            .map(o -> (Prescripcion) o)
            .filter(p -> p.getPatientId().equals(patientId))
            .sorted(Comparator.comparing(Prescripcion::getDate).reversed())
            .limit(3)
            .collect(Collectors.toList());
    }

    public boolean hasAllergy(String patientId, String medicationName) {
        return false;
    }
}