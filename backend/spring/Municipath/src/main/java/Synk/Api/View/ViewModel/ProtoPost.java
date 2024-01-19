package Synk.Api.View.ViewModel;

import java.time.LocalDateTime;
import java.util.List;

import Synk.Api.Model.Post.PostType;

public class ProtoPost {
	
    private String title;
    private PostType type;
    private String text;
    private List<String> multimediaData;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean persistence;
    
	public ProtoPost() {}

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

	public List<String> getMultimediaData() {
		return multimediaData;
	}

	public void setMultimediaData(List<String> multimediaData) {
		this.multimediaData = multimediaData;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public boolean isPersistence() {
		return persistence;
	}

	public void setPersistence(boolean persistence) {
		this.persistence = persistence;
	}
}
