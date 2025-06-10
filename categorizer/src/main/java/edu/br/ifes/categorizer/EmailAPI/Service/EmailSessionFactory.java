package edu.br.ifes.categorizer.EmailAPI.Service;

import javax.mail.Session;
import java.util.Properties;

public class EmailSessionFactory {
    public static Session createSession(String host) {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap");
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        return Session.getDefaultInstance(properties);
    }
}

