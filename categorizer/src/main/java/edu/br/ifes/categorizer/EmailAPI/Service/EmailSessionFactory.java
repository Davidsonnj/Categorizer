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

        // timeout de conexão (ms)
        properties.put("mail.imap.connectiontimeout", "10000"); // 10s
        properties.put("mail.imap.timeout", "60000");           // 60s

        // número de conexões simultâneas
        properties.put("mail.imap.connectionpoolsize", "5");

        // pré-busca de mensagens para evitar múltiplos roundtrips
        properties.put("mail.imap.fetchsize", "1048576"); // 1 MB por chunk

        // eventos IMAP
        properties.put("mail.imap.enableimapevents", "true");



        return Session.getDefaultInstance(properties);
    }
}

