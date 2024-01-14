package Synk.Api.Model.User.Notification;

import org.joda.time.DateTime;

public class Notification {

    private String author;
    private DateTime date;
    private String text;
    private String contentId;
    private boolean isRead;
    private String receiver;
    private String Id;

    public Notification(String author, DateTime date, String text, String contentId, boolean isRead,
                        String receiver, String Id) {
        this.author = author;
        this.date = date;
        this.text = text;
        this.contentId = contentId;
        this.isRead = isRead;
        this.receiver = receiver;
        this.Id = Id;
    }

    public Notification() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
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
