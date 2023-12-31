package Synk.Api.Model.Post;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Synk.Api.Model.Group.Group;

public class Post {

    private String title;
    private PostType type;
    private String author;
    private Position pos;
    private String cityID;
    private String text;
    private String postId;
    private List<String> multimediaData;
    private List<Group> groups;
    private boolean published;
    private String meteo;
    private Date start;
    private Date end;
    private boolean persistence;

    public Post(String title, PostType type, String text, String author, Position pos,
            String cityID, String postId, ArrayList<String> data, boolean published,
            Date start, Date end, boolean persistence) {
	    this.title = title;
	    this.type = type;
	    this.author = author;
	    this.pos = pos;
	    this.cityID = cityID;
	    this.postId = postId;
	    this.multimediaData = data;
	    this.published = published;
	    this.start = start;
	    this.end = end;
	    this.persistence = persistence;
	    this.groups = new ArrayList<>();
	}
	
	public Post() {}


    public List<String> getMultimediaData() {
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

	public boolean isPersistence() {
		return persistence;
	}

	public void setPersistence(boolean persistence) {
		this.persistence = persistence;
	}

	public boolean checkAuthor(String author) {
        return this.author.equals(author);
    }

    public void updateInfo(String title, PostType type, String text,
    		ArrayList<String> data, Date start, Date end, boolean persistence) {
        this.title = title;
        this.type = type;
        this.text = text;
        this.multimediaData.clear();
        this.multimediaData.addAll(data);
        this.start = start;
        this.end = end;
        this.persistence = persistence;
    }

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getMeteo() {
		return meteo;
	}

	public void setMeteo(String meteo) {
		this.meteo = meteo;
	}
	
	

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((postId == null) ? 0 : postId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Post))
			return false;
		Post other = (Post) obj;
		if (postId == null) {
			if (other.postId != null)
				return false;
		} else if (!postId.equals(other.postId))
			return false;
		return true;
	}

	public Date getMeteoDate() {
		return this.start == null ? new Date() : this.start;
	}
	
	
    
}
