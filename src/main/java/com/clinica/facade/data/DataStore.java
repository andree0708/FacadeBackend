package com.clinica.facade.data;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class DataStore {
    private static final String DATA_FILE = "clinica-data.json";
    private final ObjectMapper mapper;
    private Map<String, Object> data;
    private final Path dataPath;

    public DataStore() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        String dataDir = System.getenv("DATA_DIR") != null ? System.getenv("DATA_DIR") : ".";
        dataPath = Paths.get(dataDir, DATA_FILE);
        
        loadData();
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            if (Files.exists(dataPath)) {
                String json = new String(Files.readAllBytes(dataPath));
                data = mapper.readValue(json, Map.class);
            } else {
                data = new ConcurrentHashMap<>();
                initializeDefaultData();
            }
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            data = new ConcurrentHashMap<>();
            initializeDefaultData();
        }
    }

    private void initializeDefaultData() {
        data.put("patients", new ConcurrentHashMap<>());
        data.put("appointments", new ConcurrentHashMap<>());
        data.put("consultations", new ConcurrentHashMap<>());
        data.put("prescriptions", new ConcurrentHashMap<>());
        data.put("laboratoryResults", new ConcurrentHashMap<>());
        data.put("counters", new ConcurrentHashMap<>());
    }

    public synchronized void saveData() {
        try {
            String json = mapper.writeValueAsString(data);
            Files.write(dataPath, json.getBytes());
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getStorage(String key) {
        if (!data.containsKey(key)) {
            data.put(key, new ConcurrentHashMap<>());
        }
        return (Map<String, Object>) data.get(key);
    }

    public Map<String, Object> getCounters() {
        return getStorage("counters");
    }

    public int getNextId(String entityType) {
        Map<String, Object> counters = getCounters();
        Integer current = (Integer) counters.get(entityType);
        int nextId = (current != null ? current : 0) + 1;
        counters.put(entityType, nextId);
        saveData();
        return nextId;
    }

    public void persist() {
        saveData();
    }
}