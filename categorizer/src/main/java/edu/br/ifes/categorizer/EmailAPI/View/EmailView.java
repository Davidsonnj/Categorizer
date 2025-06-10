package edu.br.ifes.categorizer.EmailAPI.View;

import edu.br.ifes.categorizer.EmailAPI.Model.Email;
import java.util.List;

public class EmailView {
    public void displayEmails(List<Email> emails) {
        System.out.println("Número de e-mails: " + emails.size());
        for (Email email : emails) {
            System.out.println("Assunto: " + email.getSubject());
            System.out.println("De: " + email.getSender());
            System.out.println("Data: " + email.getDate());
            System.out.println("Corpo: " + email.getBody());
            System.out.println("---------------------------------");
        }
    }

    public void displaySendEmailStatus(boolean success) {
        if (success) {
            System.out.println("✅ E-mail enviado com sucesso!");
        } else {
            System.out.println("❌ Falha ao enviar o e-mail.");
        }
    }
}

