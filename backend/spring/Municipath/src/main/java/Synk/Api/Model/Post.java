package Synk.Api.Model;

import java.io.File;
import java.util.Date;

public class Post {

    String title;
    PostType type;
    File newFile;
    String author;
    Position pos;
    String cityID;
    String text;
    int postId;


    public Post(String title, PostType type, File newFile, String text, String author, Position pos,
                String cityID, int postId) {
        if (title == null || type == null || type == PostType.EVENT || newFile == null || author == null || pos == null
                || cityID == null) {
            throw new IllegalArgumentException("One of the field is null or invalid");
        }
        this.title = title;
        this.type = type;
        this.newFile = newFile;
        this.author = author;
        this.pos = pos;
        this.cityID = cityID;
        this.postId = postId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PostType getType() {
        return type;
    }

    public void setType(PostType type) {
        this.type = type;
    }

    public File getNewFile() {
        return newFile;
    }

    public void setNewFile(File newFile) {
        this.newFile = newFile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getId() {
        return cityID;
    }

    public Date getDateTime() {
        //TODO Implement here
        return null;
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
