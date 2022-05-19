package com.waifus.services;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
    private static String email = "waifusforum@gmail.com";
    private static String pass = "waifusforo0=";

    public void sendMail(String html, String to, String subject) throws MessagingException{
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        System.out.println("Sesion");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, pass);
            }
        });
        System.out.println("Mensaje");
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(html);
        System.out.println("Previo send");
        Transport.send(message);
        System.out.println("Fin");
    }

    private void sendActivationCode() throws MessagingException {

    }
}
