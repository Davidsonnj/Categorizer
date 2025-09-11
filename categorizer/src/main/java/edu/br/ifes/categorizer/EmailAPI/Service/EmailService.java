package edu.br.ifes.categorizer.EmailAPI.Service;

import edu.br.ifes.categorizer.EmailAPI.Model.Email;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EmailService {
    private String host;
    private String username;
    private String password;

    public EmailService(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    /**
     * Busca e-mails não lidos e aplica filtros opcionais (assunto, corpo, remetente).
     */
    public List<Email> fetchEmails(String subjectFilter, String bodyFilter, String senderFilter) {
        List<Email> emails = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            Session session = EmailSessionFactory.createSession(host);
            Store store = session.getStore("imap");
            store.connect(host, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            UIDFolder uidFolder = (UIDFolder) inbox;

            // Buscar apenas e-mails não lidos
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for (Message message : messages) {
                String subject = message.getSubject();
                String sender = message.getFrom()[0].toString();
                String body = limparHtml(getTextFromMessage(message));
                long uid = uidFolder.getUID(message);

                // Aplicando filtros
                boolean matchesSubject =
                        (subjectFilter == null || subject.equalsIgnoreCase(subjectFilter));
                boolean matchesBody =
                        (bodyFilter == null || body.toLowerCase().contains(bodyFilter.toLowerCase()));
                boolean matchesSender =
                        (senderFilter == null || sender.toLowerCase().contains(senderFilter.toLowerCase()));

                if (matchesSubject && matchesBody && matchesSender) {
                    List<String> attachmentPaths = new ArrayList<>();

                    if (message.isMimeType("multipart/*")) {
                        Multipart multipart = (Multipart) message.getContent();

                        // Baixa anexos em thread separada para não travar o loop
                        Future<List<String>> future = executor.submit(() -> extractAttachments(multipart, executor));
                        attachmentPaths = future.get(); // espera o download do anexo
                    }

                    emails.add(new Email(uid, subject, sender, message.getSentDate(), body, attachmentPaths));
                }
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emails;
    }

    /**
     * Extrai corpo do e-mail em texto simples.
     */
    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return (String) message.getContent();
        } else if (message.isMimeType("text/html")) {
            return (String) message.getContent();
        } else if (message.getContent() instanceof MimeMultipart) {
            return getTextFromMimeMultipart((MimeMultipart) message.getContent());
        }
        return "";
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart part = mimeMultipart.getBodyPart(i);

            if (part.isMimeType("text/plain")) {
                return (String) part.getContent();
            } else if (part.isMimeType("text/html")) {
                return (String) part.getContent();
            } else if (part.getContent() instanceof MimeMultipart) {
                return getTextFromMimeMultipart((MimeMultipart) part.getContent());
            }
        }
        return "";
    }

    /**
     * Garante que o arquivo salvo não sobrescreva outro existente.
     */
    private String getAvailableFileName(String directory, String originalName) {
        File file = new File(directory + originalName);

        if (!file.exists()) {
            return originalName;
        }

        String name = originalName;
        String extension = "";

        int dotIndex = originalName.lastIndexOf(".");
        if (dotIndex != -1) {
            name = originalName.substring(0, dotIndex);
            extension = originalName.substring(dotIndex);
        }

        int counter = 1;
        while (true) {
            String newName = String.format("%s(%d)%s", name, counter, extension);
            file = new File(directory + newName);
            if (!file.exists()) {
                return newName;
            }
            counter++;
        }
    }

    /**
     * Extrai e salva anexos dos e-mails.
     */
    private List<String> extractAttachments(Multipart multipart, ExecutorService executor) throws Exception {
        String outputDir = "/home/davidson/Desktop/Defesa-Mestrado-BPMN/Defesa-Mestrado-Camunda/anexos/";
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);

            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) && part.getFileName() != null) {
                String originalFileName = part.getFileName();
                futures.add(executor.submit(() -> downloadAttachment(part, outputDir, originalFileName)));
            }

            if (part.getContent() instanceof Multipart) {
                futures.addAll(extractAttachments((Multipart) part.getContent(), executor)
                        .stream()
                        .map(path -> executor.submit(() -> path)) // transformar em Future
                        .toList());
            }
        }

        // Espera todos terminarem e junta os caminhos
        List<String> savedFiles = new ArrayList<>();
        for (Future<String> f : futures) {
            savedFiles.add(f.get());
        }

        return savedFiles;
    }

    /**
     * Remove tags e entidades HTML.
     */
    public static String limparHtml(String html) {
        if (html == null) return "";

        // Remove tags HTML
        String texto = html.replaceAll("<[^>]+>", "");

        // Converte entidades HTML
        texto = texto.replaceAll("&#39;", "'")
                .replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&nbsp;", " ");

        // Remove espaços duplicados e quebra de linha
        return texto.replaceAll("\\s+", " ").trim();
    }

    private String downloadAttachment(BodyPart part, String outputDir, String originalFileName) throws Exception {
        String fileName = getAvailableFileName(outputDir, originalFileName);
        File file = new File(outputDir + fileName);
        file.getParentFile().mkdirs();

        try (InputStream is = part.getInputStream();
             FileOutputStream fos = new FileOutputStream(file)) {

            byte[] buffer = new byte[4 * 1024 * 1024];  // 4 MB
            int bytesRead;
            long total = 0;

            long start = System.currentTimeMillis();

            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                total += bytesRead;

                // imprime progresso em MB
                long elapsed = (System.currentTimeMillis() - start) / 1000 + 1; // segundos
                double speed = (total / 1024.0 / 1024.0) / elapsed; // MB/s

                System.out.print("\rBaixando " + file.getName() + ": "
                        + (total / (1024 * 1024)) + " MB ("
                        + String.format("%.2f", speed) + " MB/s)");
            }
        }

        System.out.println("\nDownload concluído: " + file.getName());
        return file.getAbsolutePath();
    }


}
