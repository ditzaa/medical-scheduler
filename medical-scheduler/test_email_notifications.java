// Test script to verify that emails are now being sent to doctors
import com.medisched.model.entities.Doctor;
import com.medisched.model.entities.Patient;
import com.medisched.services.email.EmailService;
import com.medisched.services.email.EmailServiceImpl;
import com.medisched.services.observer.AppointmentSubject;

public class TestEmailNotifications {
    public static void main(String[] args) {
        System.out.println("Testing Email Notifications through Observer Pattern...");
        
        // Create a doctor
        Doctor doctor = new Doctor();
        doctor.setFirstName("Dr. John");
        doctor.setLastName("Smith");
        doctor.setEmail("doctor@example.com");
        doctor.setSpecialization("Cardiology");
        
        // Create EmailService (this will use the fallback console output since email is not configured)
        EmailService emailService = new EmailServiceImpl();
        
        // Test message
        String testMessage = "Nouă programare (Standard) pe data de 2024-01-15T10:00. Preț: 200.0 RON\n\n" +
                           "Detalii pacient:\n" +
                           "Nume: Jane Doe\n" +
                           "Email: patient@example.com\n" +
                           "Vârstă: 35\n";
        
        // Test Observer pattern notification with EmailService
        AppointmentSubject subject = new AppointmentSubject();
        subject.addObserver(doctor);
        
        System.out.println("Sending notification through Observer pattern with EmailService...");
        subject.notifyObservers(testMessage, emailService);
        
        System.out.println("\nTest completed. Check console output above for email notification.");
        System.out.println("If email configuration is not set up, you should see a console message");
        System.out.println("indicating that the email would have been sent.");
    }
}