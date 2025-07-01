package edu.br.ifes.categorizer;

import edu.br.ifes.categorizer.Database.DAO.Implementations.EmailDAO;
import edu.br.ifes.categorizer.EmailAPI.Controller.EmailController;
import edu.br.ifes.categorizer.EmailAPI.Model.Email;
import edu.br.ifes.categorizer.EmailAPI.Service.MarkEmail;
import edu.br.ifes.categorizer.EmailAPI.View.EmailView;
import edu.br.ifes.categorizer.GenAI.Perguntar;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailScheduler {

    private final EmailController emailController = new EmailController();
    private final EmailDAO emailDAO = new EmailDAO();
    private final MarkEmail markEmail = new MarkEmail();

    @Scheduled(fixedRate = 10000)
    public void processEmails() {
        System.out.println("Processing emails...");
        List<Email> emails = emailController.emails(null, null, null);
        for (Email email : emails) {
            String status = Perguntar.getStatusEmail(email);
            email.setStatus(status);
            System.out.println(status);

            emailDAO.insert(email);
            markEmail.wasRead(email.getUid());
        }
    }
}