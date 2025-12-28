package com.medisched.services.command;

import com.medisched.model.entities.Appointment;
import com.medisched.repositories.AppointmentRepository;

public class EditAppointmentCommand implements Command {

    private final AppointmentRepository appointmentRepository;
    private final Appointment appointment;

    public EditAppointmentCommand(AppointmentRepository appointmentRepository, Appointment appointment) {
        this.appointmentRepository = appointmentRepository;
        this.appointment = appointment;
    }

    @Override
    public void execute() {
        // În JPA, save() face și update dacă ID-ul există deja
        appointmentRepository.save(appointment);
        System.out.println("Command: Programarea a fost actualizată cu succes în baza de date.");
    }
}
