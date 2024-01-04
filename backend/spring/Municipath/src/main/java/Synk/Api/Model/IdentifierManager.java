package Synk.Api.Model;



public class IdentifierManager {
	
	public String FromPostToPoint(String postId) {
    	String[] parts = postId.split("\\.");
    	return parts[0]+"."+parts[1];
    }
    
    
    public String getCityId(String contentId) {
    	return contentId.split("\\.")[0];
    }
    
    public boolean isGroup(String id) {
    	return id.split("\\.")[1].equals("g");
    }
    
    public String getContentId(String id) {
    	return id.split("\\.")[2];
    }
	
	
}
