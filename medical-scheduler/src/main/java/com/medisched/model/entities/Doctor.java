package com.medisched.model.entities;

import com.medisched.services.observer.AppointmentObserver;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Doctor extends User implements AppointmentObserver {

    private String specialization;
    private String cabinetNumber;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    // Implementarea metodei din interfața AppointmentObserver
    @Override
    public void update(String message) {
        // În aplicația reală, aici s-ar trimite un email sau SMS
        System.out.println("Notificare pentru Dr. " + getLastName() + ": " + message);
    }

    // Getters și Setters
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getCabinetNumber() { return cabinetNumber; }
    public void setCabinetNumber(String cabinetNumber) { this.cabinetNumber = cabinetNumber; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}