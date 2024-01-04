package Synk.Api.Model;



public class IdentifierManager {
	
	public String FromPostToPoint(String postId) {
    	String[] parts = postId.split("\\.");
    	return parts[0]+"."+parts[1];
    }
    
    
    public String getCityId(String contentId) {
    	return contentId.split("\\.")[0];
    }
	
	
}
