package com.medisched.services.impl;

import com.medisched.config.ClinicLogger;
import com.medisched.model.entities.Appointment;
import com.medisched.model.entities.Doctor;
import com.medisched.model.entities.Patient;
import com.medisched.model.protocols.MedicalProtocol;
import com.medisched.repositories.AppointmentRepository;
import com.medisched.repositories.DoctorRepository;
import com.medisched.services.email.EmailService;
import com.medisched.services.factory.AppointmentFactory;
import com.medisched.services.observer.AppointmentSubject;
import com.medisched.services.strategy.PricingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AppointmentManagerServiceImpl {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private Map<String, PricingStrategy> pricingStrategies;

    private static final double BASE_PRICE = 200.0;

    /**
     * Construiește detaliile pacientului pentru mesajele de notificare
     * @param patient pacientul pentru care se construiesc detaliile
     * @return string cu detaliile pacientului formatate
     */
    private String buildPatientDetails(Patient patient) {
        if (patient == null) {
            return "";
        }

        StringBuilder detailsBuilder = new StringBuilder();
        detailsBuilder.append("Detalii pacient:\n")
            .append("Nume: ").append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n")
            .append("Email: ").append(patient.getEmail()).append("\n");

        if (patient.getAge() != null) {
            detailsBuilder.append("Vârstă: ").append(patient.getAge()).append("\n");
        }

        if (patient.getCnp() != null && !patient.getCnp().isEmpty()) {
            detailsBuilder.append("CNP: ").append(patient.getCnp()).append("\n");
        }

        if (patient.getMedicalHistory() != null && !patient.getMedicalHistory().isEmpty()) {
            detailsBuilder.append("Istoric medical: ").append(patient.getMedicalHistory()).append("\n");
        }

        return detailsBuilder.toString();
    }

    public void processAndSaveAppointment(Appointment appointment, String strategyType) {
        // 1. UTILIZARE STRATEGY PATTERN pentru calculul prețului
        PricingStrategy strategy = pricingStrategies.getOrDefault(strategyType, pricingStrategies.get("standard"));
        double finalPrice = strategy.calculatePrice(BASE_PRICE);
        appointment.setPrice(finalPrice);

        // Salvare directă
        appointmentRepository.save(appointment);

        // 2. UTILIZARE OBSERVER PATTERN (Notificăm medicul prin email)
        if (appointment.getDoctor() != null) {
            Doctor doctor = appointment.getDoctor();

            // Construim un mesaj mai detaliat care include informații despre pacient
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("Nouă programare (" + strategy.getName() + ") pe data de ")
                    .append(appointment.getAppointmentDate())
                    .append(". Preț: ")
                    .append(finalPrice)
                    .append(" RON\n\n");

            // Adăugăm detaliile pacientului folosind metoda comună
            messageBuilder.append(buildPatientDetails(appointment.getPatient()));

            String message = messageBuilder.toString();

            // Utilizăm doar Observer pattern pentru notificări
            // Creăm un subiect și notificăm observatorul (doctorul) cu EmailService
            AppointmentSubject appointmentSubject = new AppointmentSubject();
            appointmentSubject.addObserver(doctor);
            appointmentSubject.notifyObservers(message, emailService);
        }

        // 3. UTILIZARE SINGLETON (Logare)
        ClinicLogger.getInstance().addLog("Programare creată cu succes. Strategie: " + strategy.getName() + 
                ", Preț final: " + finalPrice);
    }

    public void createMedicalAppointmentWithFactory(AppointmentFactory factory, Doctor doctor, String strategyType) {
        // 1. UTILIZARE ABSTRACT FACTORY
        Appointment appointment = factory.createAppointment();
        MedicalProtocol protocol = factory.createProtocol();

        appointment.setDoctor(doctor);
        // Setăm o dată implicită dacă nu e specificată
        if (appointment.getAppointmentDate() == null) {
            appointment.setAppointmentDate(java.time.LocalDateTime.now().plusDays(1));
        }

        // Procesăm și salvăm folosind restul pattern-urilor
        processAndSaveAppointment(appointment, strategyType);

        // Notificare suplimentară despre protocol prin Observer pattern
        StringBuilder protocolMessageBuilder = new StringBuilder();
        protocolMessageBuilder.append("Protocol medical generat: ").append(protocol.getInstructions()).append("\n\n");

        // Adăugăm detaliile pacientului folosind metoda comună
        protocolMessageBuilder.append(buildPatientDetails(appointment.getPatient()));

        String protocolMessage = protocolMessageBuilder.toString();

        // Utilizăm doar Observer pattern pentru notificări despre protocol
        AppointmentSubject appointmentSubject = new AppointmentSubject();
        appointmentSubject.addObserver(doctor);
        appointmentSubject.notifyObservers(protocolMessage, emailService);
    }
}
