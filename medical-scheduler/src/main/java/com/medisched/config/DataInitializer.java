package com.medisched.config;

import com.medisched.model.entities.Doctor;
import com.medisched.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner init(UserRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                Doctor d = new Doctor();
                d.setFirstName("Andrei");
                d.setLastName("Ionescu");
                d.setSpecialization("Cardiologie");
                repo.save(d);
                System.out.println("Medic de test salvat!");
            }
        };
    }
}