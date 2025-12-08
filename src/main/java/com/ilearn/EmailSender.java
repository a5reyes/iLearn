package com.ilearn;

import jakarta.mail.*;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String recipient, String subject, String body,
                                 String senderEmail, String senderPassword) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            
            Message email = new MimeMessage(session);

            email.setFrom(new InternetAddress(senderEmail));
            email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            email.setSubject(subject);
            email.setText(body);

            Transport.send(email);
            System.out.println("Email sent successfully to " + recipient);

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
