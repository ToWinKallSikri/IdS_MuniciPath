package Synk.Api.Model;

import java.time.LocalDateTime;

import Synk.Api.Model.Feedback.Score;

public interface MetaData {
    
	public String getId();
	
	public String getCityId();
	
	public String getAuthor();
	
	public LocalDateTime getPublicationTime();
	
	public int getViewsCount();
	
	public Score getVote();
	
	public boolean isOfCity();
    
}
