package com.medisched.services.factory;

import com.medisched.model.entities.Appointment;
import com.medisched.model.protocols.GeneralProtocol;
import com.medisched.model.protocols.MedicalProtocol;
import org.springframework.stereotype.Component;

@Component
public class GeneralFactory implements AppointmentFactory {

    @Override
    public Appointment createAppointment() {
        Appointment app = new Appointment();
        app.setType("Medicina generala");
        return app;
    }

    @Override
    public MedicalProtocol createProtocol() {
        return new GeneralProtocol();
    }
}