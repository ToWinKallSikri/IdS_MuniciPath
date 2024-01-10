package Synk.Api.Model.Group;


import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "groups")
public class Group {
	
	@Id
	private String id;
	private String title;
	private String cityId;
	private String author;
	private boolean sorted; 
	private boolean published; 
	private boolean persistence;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime publicationTime;
    @ElementCollection
    @Fetch(FetchMode.JOIN)
    private List<String> posts;
    private boolean ofCity;
    private int viewsCount;
    
    public Group() {}
    
    
	
    public Group(String id, String title, String cityId, String author, boolean sorted,
    		boolean published, boolean persistence, LocalDateTime start, 
    		LocalDateTime end, List<String> posts, boolean ofCity) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.cityId = cityId;
		this.sorted = sorted;
		this.published = published;
		this.persistence = persistence;
		this.startTime = start;
		this.endTime = end;
		this.posts = posts;
	    this.ofCity = ofCity;
	    this.viewsCount = 0;
	    this.publicationTime = LocalDateTime.now();
	}

    

	public boolean isPublished() {
		return published;
	}



	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isSorted() {
		return sorted;
	}
	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	public boolean isPersistence() {
		return persistence;
	}

	public void setPersistence(boolean persistence) {
		this.persistence = persistence;
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
	

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public void setPosts(List<String> posts) {
		this.posts = posts;
	}

	public List<String> getPosts() {
		return posts;
	}
	public void removePost(String post) {
		this.posts.remove(post);
	}
	
	public LocalDateTime getPublicationTime() {
		return publicationTime;
	}

	

	public void setPublicationTime(LocalDateTime publicationTime) {
		this.publicationTime = publicationTime;
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

	public void addOneView () {
		this.viewsCount++;
	}


	public boolean isGroup() {
		return this.posts.size() > 1;
	}

	public void edit(String title, boolean sorted, List<String> posts, LocalDateTime start, LocalDateTime end, boolean persistence) {
		this.title = title;
		this.sorted = sorted;
		this.posts = posts;
		this.startTime = start;
		this.endTime = end;
		this.persistence = persistence;
	}

}
