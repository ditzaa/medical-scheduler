package com.medisched.config;

import com.medisched.model.entities.Doctor;
import com.medisched.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner init(UserRepository repo, JdbcTemplate jdbcTemplate) {
        return args -> {
            // Sincronizăm automat secvența ID-urilor cu datele existente (pentru a evita coliziuni la inserări manuale)
            Long maxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM users", Long.class);
            if (maxId != null) {
                long nextId = maxId + 1;
                jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH " + nextId);
                System.out.println("Secvența ID-urilor a fost sincronizată la: " + nextId);
            }

            if (repo.count() == 0) {
                Doctor d = new Doctor();
                d.setFirstName("Andrei");
                d.setLastName("Ionescu");
                d.setSpecialization("Cardiologie");
                d.setEmail("andrei.ionescu@med.ro");
                d.setPassword("pass1");
                repo.save(d);
                System.out.println("Medic de test salvat!");
            }
        };
    }
}