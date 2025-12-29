package com.medisched.services.observer;

import com.medisched.services.email.EmailService;

public interface AppointmentObserver {
    void update(String message, EmailService emailService);
}
