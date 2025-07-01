package edu.br.ifes.categorizer;
import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import edu.br.ifes.categorizer.Database.DAO.Implementations.EmailDAO;
import edu.br.ifes.categorizer.EmailAPI.Controller.EmailController;
import edu.br.ifes.categorizer.EmailAPI.Model.Email;
import edu.br.ifes.categorizer.EmailAPI.Service.MarkEmail;
import edu.br.ifes.categorizer.EmailAPI.View.EmailView;
import edu.br.ifes.categorizer.GenAI.Perguntar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@SpringBootApplication
@EnableScheduling
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
