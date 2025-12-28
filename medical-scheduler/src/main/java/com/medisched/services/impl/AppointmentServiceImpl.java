package com.medisched.services.impl;

import com.medisched.config.ClinicLogger;
import com.medisched.model.entities.Appointment;
import com.medisched.model.entities.Doctor;
import com.medisched.model.protocols.MedicalProtocol;
import com.medisched.repositories.AppointmentRepository;
import com.medisched.services.command.CreateAppointmentCommand;
import com.medisched.services.factory.AppointmentFactory;
import com.medisched.services.observer.AppointmentSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AppointmentServiceImpl {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public void saveAppointment(Appointment appointment) {
        // Folosim Command Pattern pentru a salva programarea
        CreateAppointmentCommand saveCommand = new CreateAppointmentCommand(appointmentRepository, appointment);
        saveCommand.execute();

        // Notificăm medicul (Observer Pattern)
        if (appointment.getDoctor() != null) {
            appointment.getDoctor().update("Aveți o programare nouă de tip " + appointment.getType() +
                    " pe data de " + appointment.getAppointmentDate());
        }

        // Logăm acțiunea (Singleton)
        ClinicLogger.getInstance().addLog("Programare salvată pentru pacientul: " +
                (appointment.getPatient() != null ? appointment.getPatient().getLastName() : "Anonim") +
                " la Dr. " + (appointment.getDoctor() != null ? appointment.getDoctor().getLastName() : "N/A"));
    }

    public void createMedicalAppointment(AppointmentFactory factory, Doctor doctor) {
        // 1. UTILIZARE ABSTRACT FACTORY
        // Cream familia de obiecte: Programarea si Protocolul aferent
        Appointment appointment = factory.createAppointment();
        MedicalProtocol protocol = factory.createProtocol();

        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1)); // Programare pt maine

        // 2. UTILIZARE COMMAND PATTERN
        // Incapsulam actiunea de salvare intr-o comanda
        CreateAppointmentCommand saveCommand = new CreateAppointmentCommand(appointmentRepository, appointment);
        saveCommand.execute();

        // 3. UTILIZARE OBSERVER PATTERN
        // Notificam medicul despre noua programare
        AppointmentSubject subject = new AppointmentSubject();
        subject.addObserver(doctor); // Doctorul este "observatorul"
        subject.notifyObservers("Programare noua: " + appointment.getType() +
                ". Protocol: " + protocol.getInstructions());

        // 4. UTILIZARE SINGLETON
        // Logam actiunea in logger-ul global al clinicii
        ClinicLogger.getInstance().addLog("S-a creat o programare de tip " +
                appointment.getType() + " pentru Dr. " + doctor.getLastName());
    }
}