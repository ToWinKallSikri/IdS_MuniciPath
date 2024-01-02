package Synk.Api.Model.Pending;

import java.time.LocalDateTime;
import java.util.List;

import Synk.Api.Model.Post.PostType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class PendingRequest {
	
	@Id
	private String id;
	private String title;
	private String text;
	private boolean isNew;
	private boolean sorted;
	private boolean persistence;
	@Enumerated(EnumType.STRING)
	private PostType type;
    @ElementCollection
	private List<String> data;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public PendingRequest(String id) {
		this.id = id;
		this.isNew = true;
	}
	
	public PendingRequest(String id, String title, boolean sorted,
			boolean persistence, List<String> data, LocalDateTime start, LocalDateTime end) {
		this.id = id;
		this.title = title;
		this.isNew = false;
		this.sorted = sorted;
		this.persistence = persistence;
		this.data = data;
		this.startTime = start;
		this.endTime = end;
	}

	public PendingRequest(String id, String title, String text, boolean persistence,
			PostType type, List<String> data, LocalDateTime start, LocalDateTime end) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.isNew = false;
		this.persistence = persistence;
		this.type = type;
		this.data = data;
		this.startTime = start;
		this.endTime = end;
	}
	
	public PendingRequest() {}

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
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

	public PostType getType() {
		return type;
	}

	public void setType(PostType type) {
		this.type = type;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
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
	
	
	
	
}
