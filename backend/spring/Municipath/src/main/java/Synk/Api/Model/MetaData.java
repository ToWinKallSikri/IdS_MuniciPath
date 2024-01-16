package Synk.Api.Model;

import java.time.LocalDateTime;

public interface MetaData {
    
	public String getId();
	
	public String getCityId();
	
	public String getAuthor();
	
	public LocalDateTime getPublicationTime();
	
	public int getViewsCount();
	
	public float getVote();
	
	public boolean isOfCity();
    
}
