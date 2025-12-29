package com.medisched.model.entities;

import com.medisched.services.email.EmailService;
import com.medisched.services.observer.AppointmentObserver;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Entity
@Component
public class Doctor extends User implements AppointmentObserver {

    private String specialization;
    private String cabinetNumber;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    // Implementarea metodei din interfața AppointmentObserver
    @Override
    public void update(String message) {
        // Fallback pentru compatibilitate cu versiunea veche
        System.out.println("Trimitere email către Dr. " + getLastName() + " la adresa " + getEmail() + ": " + message);
    }

    @Override
    public void update(String message, EmailService emailService) {
        // Trimitem email folosind EmailService primit ca parametru
        String subject = "Notificare programare - MediSched";
        if (emailService != null) {
            emailService.sendEmail(getEmail(), subject, message);
        } else {
            // Fallback pentru cazul în care emailService este null
            System.out.println("Trimitere email către Dr. " + getLastName() + " la adresa " + getEmail() + ": " + message);
        }
    }

    // Getters și Setters
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getCabinetNumber() { return cabinetNumber; }
    public void setCabinetNumber(String cabinetNumber) { this.cabinetNumber = cabinetNumber; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}
