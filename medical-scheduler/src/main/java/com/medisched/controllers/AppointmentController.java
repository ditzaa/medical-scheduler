package com.medisched.controllers;

import com.medisched.model.entities.Doctor;
import com.medisched.repositories.UserRepository;
import com.medisched.services.factory.CardiologyFactory;
import com.medisched.services.factory.GeneralFactory;
import com.medisched.services.impl.AppointmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentServiceImpl appointmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardiologyFactory cardiologyFactory;

    @Autowired
    private GeneralFactory generalFactory;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/programare/cardiologie")
    public String createCardio(Model model) {
        // Luam primul doctor din DB sau cream unul daca nu exista (pentru test)
        Doctor doc = (Doctor) userRepository.findAll().stream()
                .filter(u -> u instanceof Doctor).findFirst().orElse(null);

        if (doc != null) {
            appointmentService.createMedicalAppointment(cardiologyFactory, doc);
            model.addAttribute("status", "Programare la Cardiologie creată cu succes!");
        } else {
            model.addAttribute("status", "Eroare: Nu există medici în baza de date!");
        }
        return "index";
    }
}