package Synk.Api.Controller;


/**
 * classe per centralizzare i controlli
 * sugli id del progetto
 */
public class IdentifierManager {
	
	/**
	 * metodo per ottenere l'id di un punto
	 * da quello di un post
	 * @param postId id del post
	 * @return id del point
	 */
	public String FromPostToPoint(String postId) {
    	String[] parts = postId.split("\\.");
    	return parts[0]+"."+parts[1];
    }
    
    /**
     * metodo per ottenere l'id
     * di un comune da un'altro id,
     * che questo sia di un punto,
     * un ruolo, un post o un group
     * @param contentId id del contenuto
     * @return id del comune
     */
    public String getCityId(String contentId) {
    	return contentId.split("\\.")[0];
    }
    
    /**
     * metodo per verificare se un dato id
     * appartiene ad un group o a un post
     * @param id id da controllare
     * @return true se appartiene ad un gruppo, false se appartiene ad un post
     */
    public boolean isGroup(String id) {
    	return id.split("\\.")[1].equals("g");
    }
    
    /**
     * metodo per ottenere l'id vero
     * e proprio di un gruppo
     * @param id id di un gruppo
     * @return suo reale id
     */
    public String getContentId(String id) {
    	return id.split("\\.")[2];
    }

	public boolean isCityFollowing(String id) {
		return id.split("\\.")[1].equals("c");
	}
	
	
}
