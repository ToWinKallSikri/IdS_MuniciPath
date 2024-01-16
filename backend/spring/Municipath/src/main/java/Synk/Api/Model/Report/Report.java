package Synk.Api.Model.Report;

import java.time.LocalDateTime;

public class Report {
	
	private String author;
    private LocalDateTime date;
    private String text;
    private String contentId;
    private boolean isRead;
    private String cityId;
    private String id;
    
	public Report(String author, String text, String contentId,String cityId) {
		this.author = author;
		this.date = LocalDateTime.now();
		this.text = text;
		this.contentId = contentId;
		this.isRead = false;
		this.cityId = cityId;
		this.id = author + "." + contentId;;
	}
	
	public Report() { }

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    
}
