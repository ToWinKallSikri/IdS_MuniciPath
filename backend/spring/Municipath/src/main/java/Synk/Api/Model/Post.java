package Synk.Api.Model;

import java.io.File;

public class Post {

        String title;
    PostType type;
    File newFile;
    String author;
    Position pos;
    String cityID;

    public Post(String title, PostType type, File newFile, String author, Position pos, String cityID) {
        this.title = title;
        this.type = type;
        this.newFile = newFile;
        this.author = author;
        this.pos = pos;
        this.cityID = cityID;
    }

    public String getId() {
        return cityID;
    }

}
