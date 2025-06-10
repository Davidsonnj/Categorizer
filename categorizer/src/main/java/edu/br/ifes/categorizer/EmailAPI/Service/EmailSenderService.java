package br.edu.ifes.mestrado.emailAPI.service;

import edu.br.ifes.categorizer.EmailAPI.Model.EmailLogin;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

public class EmailSenderService {
    private static final EmailLogin login = new EmailLogin();

    public EmailSenderService() {
    }

    public boolean sendEmail(String to, String subject, String body) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", login.smtpHost);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.ssl.trust", login.smtpHost);

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login.username, login.password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(login.username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmail(String to, String subject, String body, List<String> attachmentPaths) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", login.smtpHost);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.ssl.trust", login.smtpHost);

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login.username, login.password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(login.username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for (String filePath : attachmentPaths) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(filePath);
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
