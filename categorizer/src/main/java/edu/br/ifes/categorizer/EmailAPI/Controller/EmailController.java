package edu.br.ifes.categorizer.EmailAPI.Controller;

import edu.br.ifes.categorizer.EmailAPI.Model.Email;
import edu.br.ifes.categorizer.EmailAPI.Model.EmailLogin;
import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import edu.br.ifes.categorizer.EmailAPI.Service.EmailService;
import edu.br.ifes.categorizer.EmailAPI.View.EmailView;

import java.util.List;

public class EmailController {
    private EmailLogin emailLogin;
    private EmailService emailService;
    private EmailView emailView;
    private EmailSenderService emailSenderService;

    public EmailController() {
        emailLogin = new EmailLogin();
        emailService = new EmailService(emailLogin.imapHost, emailLogin.username, emailLogin.password);
        emailSenderService = new EmailSenderService();
    }

    public List<Email> emails(String subjectFilter, String bodyFilter, String senderFilter) {
        List<Email> emails = emailService.fetchEmails(subjectFilter, bodyFilter, senderFilter);
        System.out.println("Número de e-mails: " + emails.size());
        return emails;
    }

    public boolean sendEmail(String to, String subject, String body) {
        boolean success = emailSenderService.sendEmail(to, subject, body);
        EmailView.displaySendEmailStatus(success);
        return success;
    }
}