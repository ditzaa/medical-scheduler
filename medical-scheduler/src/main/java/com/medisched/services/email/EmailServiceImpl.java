package com.medisched.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired(required = false)
    private JavaMailSender emailSender;

    @Value("${spring.mail.username:your-email@gmail.com}")
    private String emailUsername;

    private boolean isEmailConfigured() {
        return emailSender != null && 
               emailUsername != null && 
               !emailUsername.equals("your-email@gmail.com");
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        // Check if email is properly configured
        if (!isEmailConfigured()) {
            System.out.println("Email service not properly configured. Skipping email to: " + to);
            System.out.println("Email would have been sent with subject: " + subject);
            System.out.println("Email content: " + body);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            emailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}
