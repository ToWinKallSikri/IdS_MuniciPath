package Synk.Api.Controller;

/**
 * Interfaccia di una classe che restituisce l'autore
 * di un dato contenuto
 */
public interface AuthorProvider {
	
	/**
	 * Metodo per ottenere l'autore di un contenuto
	 * @param contentId id del contenuto
	 * @return autore del contenuto
	 */
	public String getAuthor(String contentId);
	
}
