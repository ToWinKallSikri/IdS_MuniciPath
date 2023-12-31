package Synk.Api.Model.Group;


import java.util.Date;
import java.util.List;

import Synk.Api.Model.Post.Post;

public class Group {
	

	private String id, title, cityId, author;
    private boolean sorted, published, persistence;
    private Date start, end;
    private List<String> posts;
    
    public Group() {}
    
    
	
    public Group(String id, String title, String cityId, String author, boolean sorted,
    		boolean published, boolean persistence, Date start, Date end, List<String> posts) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.cityId = cityId;
		this.sorted = sorted;
		this.published = published;
		this.persistence = persistence;
		this.start = start;
		this.end = end;
		this.posts = posts;
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

	public boolean isGroup() {
		return this.posts.size() > 1;
	}

	public void edit(String title, boolean sorted, List<String> posts, Date start, Date end, boolean persistence) {
		this.title = title;
		this.sorted = sorted;
		this.posts = posts;
		this.start = start;
		this.end = end;
		this.persistence = persistence;
	}
}
