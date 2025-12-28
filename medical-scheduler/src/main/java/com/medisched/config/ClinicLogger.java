package com.medisched.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClinicLogger {
    private static ClinicLogger instance;
    private List<String> logs = new ArrayList<>();

    private ClinicLogger() {} // Constructor privat

    public static synchronized ClinicLogger getInstance() {
        if (instance == null) instance = new ClinicLogger();
        return instance;
    }

    public void addLog(String message) {
        logs.add(LocalDateTime.now() + ": " + message);
        System.out.println(message);
    }
}
