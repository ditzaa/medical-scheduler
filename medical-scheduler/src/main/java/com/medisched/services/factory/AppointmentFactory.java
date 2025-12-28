package com.medisched.services.factory;

import com.medisched.model.entities.Appointment;
import com.medisched.model.protocols.MedicalProtocol;

public interface AppointmentFactory {
    Appointment createAppointment();
    MedicalProtocol createProtocol();
}

