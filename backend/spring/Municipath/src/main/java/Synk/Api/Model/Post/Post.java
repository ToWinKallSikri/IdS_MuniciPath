package Synk.Api.Model.Post;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import Synk.Api.Model.MetaData;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.View.Model.ProtoPost;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Post implements MetaData {
	

    @Id
    private String id;
    private String pointId;
    private String cityId;
    private String title;
	@Enumerated(EnumType.STRING)
    private PostType type;
    private String author;
    @Embedded
    private Position pos;
    private String text;
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
    private LocalDateTime publicationTime;
    private boolean persistence;
    private boolean ofCity;
    private int viewsCount;
    @Transient
    private float vote;
    
	public Post() {
	    this.groups = new ArrayList<>();
	    this.viewsCount = 0;
	    this.publicationTime = LocalDateTime.now();
	    this.vote = 0;
	}


    public List<String> getMultimediaData() {
		return multimediaData;
	}

	public void setMultimediaData(List<String> multimediaData) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
	

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public boolean isOfCity() {
		return ofCity;
	}

	public void setOfCity(boolean ofCity) {
		this.ofCity = ofCity;
	}

	public int getViewsCount() {
		return viewsCount;
	}

	public void setViewsCount(int viewsCount) {
		this.viewsCount = viewsCount;
	}
	
	public LocalDateTime getPublicationTime() {
		return publicationTime;
	}

	public void setPublicationTime(LocalDateTime publicationTime) {
		this.publicationTime = publicationTime;
	}

	public void addOneView () {
		this.viewsCount++;
	}

	public float getVote() {
		return vote;
	}


	public void setVote(float vote) {
		this.vote = vote;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Post))
			return false;
		Post other = (Post) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
	 * dati i seguenti parametri, li
	 * usa per aggiornare il post
	 * @param data nuovi dati del post
	 */
    public void updateInfo(ProtoPost data) {
        this.title = data.getTitle();
        this.type = data.getType();
        this.text = data.getText();
        this.multimediaData.clear();
        this.multimediaData.addAll(data.getMultimediaData());
        this.startTime = data.getStartTime();
        this.endTime = data.getEndTime();
        this.persistence = data.isPersistence();
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
