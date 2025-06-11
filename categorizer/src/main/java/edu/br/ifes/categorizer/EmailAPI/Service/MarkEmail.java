package edu.br.ifes.categorizer.EmailAPI.Service;

import edu.br.ifes.categorizer.EmailAPI.Model.EmailLogin;

import javax.mail.*;

public class MarkEmail {

    public void wasRead(long uid) {
        EmailLogin emailLogin = new EmailLogin();
        try {
            Session session = EmailSessionFactory.createSession(emailLogin.imapHost);
            Store store = session.getStore("imap");
            store.connect(emailLogin.imapHost, emailLogin.username, emailLogin.password);

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            UIDFolder uidFolder = (UIDFolder) folder;
            Message message = uidFolder.getMessageByUID(uid);

            if (message != null) {
                message.setFlag(Flags.Flag.SEEN, true); // marca como lido
                System.out.println("E-mail com UID " + uid + " marcado como lido.");
            } else {
                System.out.println("Nenhum e-mail encontrado com UID " + uid);
            }

            folder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}