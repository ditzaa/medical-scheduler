package com.medisched.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id // Aceasta este adnotarea care lipsea sau genera eroarea
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Spune bazei de date să incrementeze automat ID-ul
    private Long id;

    private String type;
    private LocalDateTime appointmentDate;
    private Double price;

    // Relațiile cu Doctor și Patient
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Constructor gol (OBLIGATORIU pentru JPA)
    public Appointment() {
    }

    // Getters și Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}