package Synk.Api.Model.Report;

import org.joda.time.DateTime;

public class Report {
	
	private String author;
    private DateTime date;
    private String text;
    private String contentId;
    private boolean isRead;
    private String cityId;
    private String Id;
    
	public Report(String author, DateTime date, String text, String contentId,
										boolean isRead, String cityId, String id) {
		this.author = author;
		this.date = date;
		this.text = text;
		this.contentId = contentId;
		this.isRead = isRead;
		this.cityId = cityId;
		Id = id;
	}
	
	public Report() { }

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
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
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
    
}
