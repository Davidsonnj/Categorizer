package edu.br.ifes.categorizer;
import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import edu.br.ifes.categorizer.Database.DAO.Implementations.EmailDAO;
import edu.br.ifes.categorizer.EmailAPI.Controller.EmailController;
import edu.br.ifes.categorizer.EmailAPI.Model.Email;
import edu.br.ifes.categorizer.EmailAPI.Service.MarkEmail;
import edu.br.ifes.categorizer.EmailAPI.View.EmailView;
import edu.br.ifes.categorizer.GenAI.Perguntar;

import java.util.List;

public class App 
{
    public static void main(String[] args) {
        EmailController emailController = new EmailController();

        List<Email> emails =emailController.emails(null,null,null);
        for (Email email : emails){
            EmailDAO emailDAO = new EmailDAO();
            MarkEmail markEmail = new MarkEmail();
            String status = Perguntar.getStatusEmail(email);

            email.setStatus(status);

            emailDAO.insert(email);
            markEmail.wasRead(email.getUid());

        }
        /*
        emailController.sendEmail("davidsonifes@gmail.com", "Teste", "teste");
        EmailDAO emailDAO = new EmailDAO();
        EmailView.displayEmails(emailDAO.findAll());

         */

    }
}
