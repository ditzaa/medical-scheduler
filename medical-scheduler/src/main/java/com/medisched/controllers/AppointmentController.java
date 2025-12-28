package com.medisched.controllers;

import com.medisched.model.entities.Appointment;
import com.medisched.model.entities.Doctor;
import com.medisched.model.entities.Patient;
import com.medisched.repositories.AppointmentRepository;
import com.medisched.repositories.DoctorRepository;
import com.medisched.repositories.PatientRepository;
import com.medisched.repositories.UserRepository;
import com.medisched.services.factory.AppointmentFactory;
import com.medisched.services.factory.CardiologyFactory;
import com.medisched.services.factory.GeneralFactory;
import com.medisched.services.impl.AppointmentManagerServiceImpl;
import com.medisched.services.impl.AppointmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentServiceImpl appointmentService;

    @Autowired
    private AppointmentManagerServiceImpl appointmentManagerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardiologyFactory cardiologyFactory;

    @Autowired
    private GeneralFactory generalFactory;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/programare/cardiologie")
    public String createCardio(Model model) {
        // Luam primul doctor din DB sau cream unul daca nu exista (pentru test)
        Doctor doc = doctorRepository.findAll().stream().findFirst().orElse(null);

        if (doc != null) {
            appointmentManagerService.createMedicalAppointmentWithFactory(cardiologyFactory, doc, "standard");
            model.addAttribute("status", "Programare la Cardiologie creată cu succes!");
        } else {
            model.addAttribute("status", "Eroare: Nu există medici în baza de date!");
        }
        return "index";
    }

    @GetMapping("/pacients")
    public String showForm(Model model) {
        model.addAttribute("allDoctors", doctorRepository.findAll());
        return "appointment_form";
    }

    @PostMapping("/appointment/save")
    public String saveAppointment(@RequestParam Long doctorId,
                                  @RequestParam String specialization,
                                  @RequestParam String date,
                                  @RequestParam String time,
                                  @RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam Integer age,
                                  @RequestParam String patientStatus,
                                  Model model) {
        
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        
        if (doctor != null) {
            // Încercăm să găsim pacientul după nume
            Patient patient = patientRepository.findByFirstNameAndLastName(firstName, lastName).orElse(null);

            if (patient == null) {
                // Dacă nu există ca pacient, creăm unul nou
                patient = new Patient();
                patient.setFirstName(firstName);
                patient.setLastName(lastName);
                patient.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com");
                patient.setPassword("pass123"); // Parolă default pentru pacienți noi
            }
            
            // Actualizam varsta (fie ca e nou sau existent)
            patient.setAge(age);
            
            try {
                patientRepository.save(patient);
            } catch (Exception e) {
                model.addAttribute("status", "Eroare critică la salvarea pacientului: " + e.getMessage());
                return "index";
            }

            // UTILIZARE ABSTRACT FACTORY pentru crearea obiectului Appointment
            AppointmentFactory factory = 
                    specialization.equals("Cardiologie") ? cardiologyFactory : generalFactory;
            
            Appointment appointment = factory.createAppointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient); // Legam pacientul
            
            // Combinăm data și ora
            LocalDateTime appointmentDateTime = LocalDateTime.parse(date + "T" + time + ":00");
            appointment.setAppointmentDate(appointmentDateTime);
            
            // UTILIZARE STRATEGY PATTERN (prin serviciu)
            appointmentManagerService.processAndSaveAppointment(appointment, patientStatus);
            
            model.addAttribute("status", "Programarea a fost salvată cu succes pentru pacientul " + 
                    firstName + " " + lastName + " (Preț calculat: " + appointment.getPrice() + " RON)!");
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

    // Editare programare - Afișare formular
    @GetMapping("/appointment/edit/{id}")
    public String editAppointmentForm(@PathVariable Long id, Model model) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment != null) {
            model.addAttribute("appointment", appointment);
            model.addAttribute("allDoctors", doctorRepository.findAll());
            // Trimitem data și ora separat pentru input-urile HTML
            model.addAttribute("datePart", appointment.getAppointmentDate().toLocalDate());
            model.addAttribute("timePart", appointment.getAppointmentDate().toLocalTime());
            return "appointment_edit";
        }
        return "redirect:/medics";
    }

    // Procesare actualizare programare
    @PostMapping("/appointment/update")
    public String updateAppointment(@RequestParam Long id,
                                   @RequestParam Long doctorId,
                                   @RequestParam String date,
                                   @RequestParam String time,
                                   Model model) {
        
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (appointment != null && doctor != null) {
            appointment.setDoctor(doctor);
            LocalDateTime appointmentDateTime = LocalDateTime.parse(date + "T" + time + ":00");
            appointment.setAppointmentDate(appointmentDateTime);
            
            appointmentService.updateAppointment(appointment);
            model.addAttribute("status", "Programarea a fost actualizată cu succes!");
        } else {
            model.addAttribute("status", "Eroare: Date invalide pentru actualizare!");
        }
        
        return "index";
    }

    // Anulare programare
    @PostMapping("/appointment/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id, Model model) {
        appointmentService.cancelAppointment(id);
        model.addAttribute("status", "Programarea a fost anulată!");
        return "index";
    }
}