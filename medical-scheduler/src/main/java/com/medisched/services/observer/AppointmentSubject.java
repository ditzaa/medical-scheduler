package com.medisched.services.observer;

import com.medisched.services.email.EmailService;
import java.util.ArrayList;
import java.util.List;

public class AppointmentSubject {
    private List<AppointmentObserver> observers = new ArrayList<>();

    public void addObserver(AppointmentObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(AppointmentObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message, EmailService emailService) {
        for (AppointmentObserver observer : observers) {
            observer.update(message, emailService);
        }
    }
}
