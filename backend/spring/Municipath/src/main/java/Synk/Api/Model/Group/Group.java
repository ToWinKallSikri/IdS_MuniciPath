package Synk.Api.Model.Group;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import Synk.Api.Model.MetaData;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.ViewModel.ProtoGroup;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "groups")
public class Group implements MetaData {
	
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
    @Transient
    private float vote;
    
    public Group(String id, String cityId, String author, boolean publish, boolean ofCity, ProtoGroup data) {
    	this.id = id;
		this.title = data.getTitle();
		this.author = author;
		this.cityId = cityId;
		this.sorted = data.isSorted();
		this.published = publish;
		this.persistence = data.isPersistence();
		this.startTime = data.getStartTime();
		this.endTime = data.getEndTime();
		this.posts = makeNewList(data.getPosts());
	    this.ofCity = ofCity;
	    this.viewsCount = 0;
	    this.publicationTime = LocalDateTime.now();
	    this.vote = 0;
    }
    
    public Group () {}

    

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

	public float getVote() {
		return vote;
	}

	public void setVote(float vote) {
		this.vote = vote;
	}
	
	

	public void edit(ProtoGroup data) {
		this.title = data.getTitle();
		this.sorted = data.isSorted();
		this.posts = makeNewList(data.getPosts());
		this.startTime = data.getStartTime();
		this.endTime = data.getEndTime();
		this.persistence = data.isPersistence();
	}


	public void edit(PendingRequest request) {
		this.title = request.getTitle();
		this.sorted = request.isSorted();
		this.posts = makeNewList(request.getData());
		this.startTime = request.getStartTime();
		this.endTime = request.getEndTime();
		this.persistence = request.isPersistence();
	}
	
	private List<String> makeNewList(List<String> oldList){
		List<String> newList = new ArrayList<>();
		newList.addAll(oldList);
		return newList;
	}

}
