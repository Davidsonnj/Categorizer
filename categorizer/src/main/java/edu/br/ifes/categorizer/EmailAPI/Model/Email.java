package edu.br.ifes.categorizer.EmailAPI.Model;

import java.util.Date;
import java.util.List;

public class Email {
    private long uid;
    private String subject;
    private String sender;
    private Date date;
    private String body;
    private List<String> attachmentPaths;
    private String status;

    public Email(){}

    public long getUid() { return uid; }
    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }
    public String getSubject() { return subject; }
    public String getSender() { return sender; }
    public Date getDate() { return date; }
    public String getBody() { return body; }
    public String getStatus() { return status; }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Email(long uid, String subject, String sender, Date date, String body, List<String> attachmentPaths) {
        this.uid = uid;
        this.subject = subject;
        this.sender = sender;
        this.date = date;
        this.body = body;
        this.attachmentPaths = attachmentPaths;
    }

    public Email(long uid, String subject, String sender, Date date, String body) {
        this.uid = uid;
        this.subject = subject;
        this.sender = sender;
        this.date = date;
        this.body = body;
    }

}
