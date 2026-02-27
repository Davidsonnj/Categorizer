package edu.br.ifes.categorizer.EmailAPI.Model;
import io.github.cdimascio.dotenv.Dotenv;

public class EmailLogin {
    
    private static final Dotenv dotenv = Dotenv.load();

    public String imapHost = "imap.gmail.com";
    public String smtpHost = "smtp.gmail.com"; // Para envio
    public String username = dotenv.get("EMAIL_LAB");
    public String password = dotenv.get("PASSWORD_LAB");
}

