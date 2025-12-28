package com.medisched.controllers;

import com.medisched.model.entities.Appointment;
import com.medisched.model.entities.Doctor;
import com.medisched.model.entities.Patient;
import com.medisched.repositories.AppointmentRepository;
import com.medisched.repositories.PatientRepository;
import com.medisched.repositories.UserRepository;
import com.medisched.services.factory.CardiologyFactory;
import com.medisched.services.factory.GeneralFactory;
import com.medisched.services.impl.AppointmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

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

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

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

    // 2. Afișare formular programare (Pacient)
    @GetMapping("/pacients")
    public String showForm(Model model) {
        model.addAttribute("allDoctors", userRepository.findAll().stream()
                .filter(u -> u instanceof Doctor)
                .toList());
        return "appointment_form";
    }

    // Procesare salvare programare din formular
    @PostMapping("/appointment/save")
    public String saveAppointment(@RequestParam Long doctorId,
                                  @RequestParam String specialization,
                                  @RequestParam String date,
                                  @RequestParam String time,
                                  @RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam Integer age,
                                  Model model) {
        
        Doctor doctor = (Doctor) userRepository.findById(doctorId).orElse(null);
        
        if (doctor != null) {
            // Logica pentru pacient: cautam sau cream
            Patient patient = patientRepository.findByFirstNameAndLastName(firstName, lastName)
                    .orElseGet(() -> {
                        Patient newPatient = new Patient();
                        newPatient.setFirstName(firstName);
                        newPatient.setLastName(lastName);
                        return newPatient;
                    });
            
            // Actualizam varsta (fie ca e nou sau existent)
            patient.setAge(age);
            patientRepository.save(patient);

            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient); // Legam pacientul
            appointment.setType(specialization);
            
            // Combinăm data și ora
            LocalDateTime appointmentDateTime = LocalDateTime.parse(date + "T" + time + ":00");
            appointment.setAppointmentDate(appointmentDateTime);
            
            appointmentService.saveAppointment(appointment);
            model.addAttribute("status", "Programarea a fost salvată cu succes pentru pacientul " + 
                    firstName + " " + lastName + " (vârstă: " + age + ")!");
        } else {
            model.addAttribute("status", "Eroare: Medicul selectat nu a fost găsit!");
        }
        
        return "index";
    }

    // 3. Vizualizare programări (Medic)
    @GetMapping("/medics")
    public String showAgenda(Model model) {
        model.addAttribute("listaProgramari", appointmentRepository.findAll());
        return "appointments";
    }
}