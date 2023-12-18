package Synk.Api.Model;

import java.io.File;

public class Event extends Post {

    public Event(String title, PostType type, File newFile, String author, Position pos, String cityID) {
        super(title, type, newFile, author, pos, cityID);
        
    }

}
