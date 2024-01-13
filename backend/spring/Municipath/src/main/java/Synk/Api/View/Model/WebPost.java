package Synk.Api.View.Model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import Synk.Api.Model.Post.PostType;

public class WebPost {
	
	private String title;
	private String type;
	private String text;
	private List<MultipartFile> data;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private boolean persistence;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<MultipartFile> getData() {
		return data;
	}
	public void setData(List<MultipartFile> data) {
		this.data = data;
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
	
	public ProtoPost buildProtoPost(List<String> multimediaData) {
		ProtoPost data = new ProtoPost();
		data.endTime = this.endTime;
		data.startTime = this.startTime;
		data.multimediaData = multimediaData;
		data.persistence = this.persistence;
		data.text = this.text;
		data.title = this.title;
		data.type = PostType.safeValueOf(this.type);
		return data;
	}
	
}
