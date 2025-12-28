package com.medisched.services.impl;

import com.medisched.model.entities.Appointment;
import com.medisched.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppointmentServiceImpl {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public void updateAppointment(Appointment appointment) {
        // Salvare directă fără Command Pattern
        appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long appointmentId) {
        // Ștergere directă fără Command Pattern
        appointmentRepository.deleteById(appointmentId);
    }
}