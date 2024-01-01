package Synk.Api.Model.Pending;

import java.time.LocalDateTime;
import java.util.List;

import Synk.Api.Model.Post.PostType;

public class PendingRequest {
	
	private String id, title, text;
	private boolean isNew, sorted, persistence;
	private PostType type;
	private List<String> data;
	private LocalDateTime startTime, endTime;
	
	public PendingRequest(String id, boolean isNew) {
		this.id = id;
		this.isNew = isNew;
	}
	
	public PendingRequest(String id, String title, boolean isNew, boolean sorted,
			boolean persistence, List<String> data, LocalDateTime start, LocalDateTime end) {
		this.id = id;
		this.title = title;
		this.isNew = isNew;
		this.sorted = sorted;
		this.persistence = persistence;
		this.data = data;
		this.startTime = start;
		this.endTime = end;
	}

	public PendingRequest(String id, String title, String text, boolean isNew, boolean persistence,
			PostType type, List<String> data, LocalDateTime start, LocalDateTime end) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.isNew = isNew;
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
