package com.clinica.facade.service;

import com.clinica.facade.entity.ResultadoLaboratorio;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class LaboratoryService {
    private final Map<String, ResultadoLaboratorio> results = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    private static final Map<String, double[]> REFERENCE_RANGES = new HashMap<>();
    
    static {
        REFERENCE_RANGES.put("hemograma", new double[]{4.5, 5.5});
        REFERENCE_RANGES.put("glicemia", new double[]{70, 100});
        REFERENCE_RANGES.put("perfil lipídico", new double[]{0, 200});
    }

    public List<ResultadoLaboratorio> request(String patientId, List<String> tests) {
        List<ResultadoLaboratorio> requestedResults = new ArrayList<>();
        Random random = new Random();
        
        for (String test : tests) {
            String testType = test.toLowerCase();
            double[] range = REFERENCE_RANGES.getOrDefault(testType, new double[]{0, 100});
            
            double value = range[0] + (range[1] - range[0]) * (0.5 + random.nextDouble() * 0.5);
            String unit = getUnit(testType);
            
            String id = "LAB-" + idCounter.getAndIncrement();
            ResultadoLaboratorio result = new ResultadoLaboratorio(
                id, patientId, test, LocalDateTime.now(),
                value, unit, range[0], range[1]
            );
            results.put(id, result);
            requestedResults.add(result);
        }
        return requestedResults;
    }

    private String getUnit(String testType) {
        switch (testType) {
            case "hemograma": return "millones/mcL";
            case "glicemia": return "mg/dL";
            case "perfil lipídico": return "mg/dL";
            default: return "mg/dL";
        }
    }

    public List<ResultadoLaboratorio> getPatientResults(String patientId) {
        return results.values().stream()
            .filter(r -> r.getPatientId().equals(patientId))
            .sorted(Comparator.comparing(ResultadoLaboratorio::getDate).reversed())
            .collect(Collectors.toList());
    }

    public List<ResultadoLaboratorio> getRecentResults(String patientId) {
        return results.values().stream()
            .filter(r -> r.getPatientId().equals(patientId))
            .sorted(Comparator.comparing(ResultadoLaboratorio::getDate).reversed())
            .limit(5)
            .collect(Collectors.toList());
    }
}