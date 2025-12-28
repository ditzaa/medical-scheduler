package com.medisched.services.command;

import com.medisched.model.entities.Appointment;
import com.medisched.repositories.AppointmentRepository;

public class CreateAppointmentCommand implements Command {

    private final AppointmentRepository appointmentRepository;
    private final Appointment appointment;

    // Constructorul prin care injectăm dependențele necesare
    public CreateAppointmentCommand(AppointmentRepository appointmentRepository, Appointment appointment) {
        this.appointmentRepository = appointmentRepository;
        this.appointment = appointment;
    }

    @Override
    public void execute() {
        // Aici se face salvarea efectivă în baza de date H2
        appointmentRepository.save(appointment);
        System.out.println("Command: Programarea a fost salvată cu succes în baza de date.");
    }
}