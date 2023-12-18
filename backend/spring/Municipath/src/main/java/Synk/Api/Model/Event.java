package Synk.Api.Model;

import java.io.File;
import java.util.Date;

public class Event extends Post {
    String title;
    PostType type;
    File newFile;
    String author;
    Position pos;
    String cityID;
    int postId;
    DateTime start;
    DateTime end;
    boolean persistence;


    public Event(String title, PostType type, File newFile, String text, String author, Position pos, String cityID, int postId,
                 DateTime start, DateTime end, boolean persistence) {
        super(title, type, newFile, text, author, pos, cityID, postId);
        this.start = start;
        this.end = end;
        this.persistence = persistence;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public PostType getType() {
        return type;
    }

    @Override
    public void setType(PostType type) {
        this.type = type;
    }

    @Override
    public File getNewFile() {
        return newFile;
    }

    @Override
    public void setNewFile(File newFile) {
        this.newFile = newFile;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public Position getPos() {
        return pos;
    }

    @Override
    public void setPos(Position pos) {
        this.pos = pos;
    }

    @Override
    public String getCityID() {
        return cityID;
    }

    @Override
    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    @Override
    public int getPostId() {
        return postId;
    }

    @Override
    public void setPostId(int postId) {
        this.postId = postId;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public boolean isPersistence() {
        return persistence;
    }

    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }

    //metodo inserito per fluidità di codice -> da sostituire con getDateTime
    public Date getStartDate() {
        return start.getDate();
    }
    //metodo inserito per fluidità di codice -> da sostituire con getDateTime
    public Date getEndDate() {
        return end.getDate();
    }

    //metodo inserito per fluidità di codice -> da sostituire con setDateTime
    public void setStartDate(Date date) {
        start.setDate(date);
    }

    //metodo inserito per fluidità di codice -> da sostituire con setDateTime
    public void setEndDate(Date date) {
        end.setDate(date);
    }

    public boolean checkAuthor(String author) {
        return this.author.equals(author);
    }

    public void updateInfo(String title, PostType type, File newFile, String text) {
        this.title = title;
        this.type = type;
        this.newFile = newFile;
        this.text = text;
    }

}
