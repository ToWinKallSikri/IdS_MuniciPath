package Synk.Api.Model.Post;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import Synk.Api.Model.Pending.PendingRequest;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Post {
	
	
    private String title;
	@Enumerated(EnumType.STRING)
    private PostType type;
    private String author;
    @Embedded
    private Position pos;
    private String cityID;
    private String text;
    @Id
    private String postId;
    @ElementCollection
    @Fetch(FetchMode.JOIN)
    private List<String> multimediaData;
    @Transient
    private List<String> groups;
    private boolean published;
    @Transient
    private String meteo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean persistence;

    public Post(String title, PostType type, String text, String author, Position pos,
            String cityID, String postId, List<String> data, boolean published,
            LocalDateTime start, LocalDateTime end, boolean persistence) {
	    this.title = title;
	    this.type = type;
	    this.author = author;
	    this.text = text;
	    this.pos = pos;
	    this.cityID = cityID;
	    this.postId = postId;
	    this.multimediaData = data;
	    this.published = published;
	    this.startTime = start;
	    this.endTime = end;
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

    public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime start) {
		this.startTime = start;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime end) {
		this.endTime = end;
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
	
	

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
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
	
	/**
	 * restituisce il momento di cui si vuole sapere il meteo
	 * @return adesso se il momento di inizio e' null, oppure il momento di inizio
	 */
	public LocalDateTime getMeteoDate() {
		return this.startTime == null ? LocalDateTime.now() : this.startTime;
	}
	

	/**
	 * dati i seugenti parametri, li
	 * usa per aggiornare il post
	 * @param title nuovo titolo
	 * @param type nuovo tipo
	 * @param text nuovo testo
	 * @param data nuovi contenuti
	 * @param start nuovo momento di inizio
	 * @param end nuovo momento di fine
	 * @param persistence se e' persistente
	 */
    public void updateInfo(String title, PostType type, String text,
    		List<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
        this.title = title;
        this.type = type;
        this.text = text;
        this.multimediaData.clear();
        this.multimediaData.addAll(data);
        this.startTime = start;
        this.endTime = end;
        this.persistence = persistence;
    }
    
    /**
     * data una pending request, la usa per aggiornare le sue info 
     * in accordo alla pending request
     * @param request richiesta di aggiornamento da usare
     */
    public void updateInfo(PendingRequest request) {
		this.title = request.getTitle();
		this.type = request.getType();
		this.text = request.getText();
		this.multimediaData.clear();
		this.multimediaData.addAll(request.getData());
		this.startTime = request.getStartTime();
		this.endTime = request.getEndTime();
		this.persistence = request.isPersistence();
	}
    
}
