package com.medisched.services.impl;

import com.medisched.config.ClinicLogger;
import com.medisched.model.entities.Appointment;
import com.medisched.model.entities.Doctor;
import com.medisched.model.protocols.MedicalProtocol;
import com.medisched.repositories.AppointmentRepository;
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
    private Map<String, PricingStrategy> pricingStrategies;

    private static final double BASE_PRICE = 200.0;

    public void processAndSaveAppointment(Appointment appointment, String strategyType) {
        // 1. UTILIZARE STRATEGY PATTERN pentru calculul prețului
        PricingStrategy strategy = pricingStrategies.getOrDefault(strategyType, pricingStrategies.get("standard"));
        double finalPrice = strategy.calculatePrice(BASE_PRICE);
        appointment.setPrice(finalPrice);

        // Salvare directă (am eliminat Command Pattern)
        appointmentRepository.save(appointment);

        // 2. UTILIZARE OBSERVER PATTERN (Notificăm medicul)
        if (appointment.getDoctor() != null) {
            appointment.getDoctor().update("Nouă programare (" + strategy.getName() + ") pe data de " 
                    + appointment.getAppointmentDate() + ". Preț: " + finalPrice + " RON");
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

        // Notificare suplimentară despre protocol
        AppointmentSubject subject = new AppointmentSubject();
        subject.addObserver(doctor);
        subject.notifyObservers("Protocol medical generat: " + protocol.getInstructions());
    }
}
