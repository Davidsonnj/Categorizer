package edu.br.ifes.categorizer;
import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import edu.br.ifes.categorizer.EmailAPI.Controller.EmailController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        EmailController emailController = new EmailController();

        boolean enviado = emailController.sendEmail("davidsonifes@gmail.com", "Assunto Teste", "Corpo do e-mail");
        System.out.println(enviado ? "✅ Enviado com sucesso" : "❌ Falha no envio");
    }
}
