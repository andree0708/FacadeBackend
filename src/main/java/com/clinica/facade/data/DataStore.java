package com.clinica.facade.data;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DataStore {
    private static final Logger logger = LoggerFactory.getLogger(DataStore.class);
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
        
        logger.info("DataStore initialized with path: {}", dataPath);
        loadData();
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            if (Files.exists(dataPath)) {
                logger.info("Loading existing data from: {}", dataPath);
                String json = new String(Files.readAllBytes(dataPath));
                data = mapper.readValue(json, Map.class);
                logger.info("Data loaded successfully. Keys: {}", data.keySet());
            } else {
                logger.info("No existing data file found, creating new");
                data = new ConcurrentHashMap<>();
                initializeDefaultData();
            }
        } catch (Exception e) {
            logger.error("Error loading data: {}", e.getMessage(), e);
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
            logger.debug("Data saved to: {}", dataPath);
        } catch (Exception e) {
            logger.error("Error saving data: {}", e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getStorage(String key) {
        if (!data.containsKey(key)) {
            logger.debug("Creating new storage for key: {}", key);
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