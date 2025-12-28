package com.medisched.services.command;

import com.medisched.repositories.AppointmentRepository;

public class CancelAppointmentCommand implements Command {

    private final AppointmentRepository appointmentRepository;
    private final Long appointmentId;

    public CancelAppointmentCommand(AppointmentRepository appointmentRepository, Long appointmentId) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentId = appointmentId;
    }

    @Override
    public void execute() {
        appointmentRepository.deleteById(appointmentId);
        System.out.println("Command: Programarea cu ID-ul " + appointmentId + " a fost anulată (ștearsă).");
    }
}
