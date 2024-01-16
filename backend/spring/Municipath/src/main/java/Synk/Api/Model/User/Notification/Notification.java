package Synk.Api.Model.User.Notification;

import java.time.LocalDateTime;

public class Notification {

    private String author;
    private LocalDateTime date;
    private String text;
    private String contentId;
    private boolean isRead;
    private String receiver;
    private String Id;

    public Notification(String author, String text, String contentId, String receiver) {
        this.author = author;
        this.date = LocalDateTime.now();
        this.text = text;
        this.contentId = contentId;
        this.isRead = false;
        this.receiver = receiver;
        this.Id = contentId + "." + date.getNano();
    }

    public Notification() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
