package Synk.Api.Model;


import java.util.ArrayList;
import java.util.Date;

public class Post {

    private String title;
    private PostType type;
    private String author;
    private Position pos;
    private String cityID;
    private String text;
    private String postId;
    private ArrayList<String> multimediaData;



	public Post(String title, PostType type, String text, String author, Position pos,
                String cityID, String postId) {
        this.title = title;
        this.type = type;
        this.author = author;
        this.pos = pos;
        this.cityID = cityID;
        this.postId = postId;
        this.multimediaData = new ArrayList<>();
    }
	
	public Post() {}
	


    public ArrayList<String> getMultimediaData() {
		return multimediaData;
	}


	public void setMultimediaData(ArrayList<String> multimediaData) {
		this.multimediaData = multimediaData;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getId() {
        return cityID;
    }

    public Date getDateTime() {
        return new Date();
    }

    public boolean checkAuthor(String author) {
        return this.author.equals(author);
    }

    public void updateInfo(String title, PostType type, String text) {
        this.title = title;
        this.type = type;
        this.text = text;
    }

}
