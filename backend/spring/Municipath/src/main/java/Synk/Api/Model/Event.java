package Synk.Api.Model;


import java.util.ArrayList;
import java.util.Date;

public class Event extends Post {
	
    private Date start;
    private Date end;
    private boolean persistence;


    public Event(String title, PostType type, String text, String author, Position pos,
            	String cityID, String postId, ArrayList<String> data, boolean published,
                 Date start, Date end, boolean persistence) {
        super(title, type, text, author, pos, cityID, postId, data, published);
        this.start = start;
        this.end = end;
        this.persistence = persistence;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean getPersistence() {
        return persistence;
    }

    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }
    
    @Override
    public Date getDateTime() {
        return this.start;
    }
}
