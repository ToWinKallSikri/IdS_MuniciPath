package Synk.Api.Controller.Post;

import java.time.LocalDateTime;
import java.util.List;

import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;

public abstract class PostCreator {
	
	/**
	 * post che viene creato da questa clsse
	 */
	protected Post post;
	
	/**
	 * metodo per la creazione di un nuovo post
	 */
	public void initializePost() {
		this.post = new Post();
	}
	
	/**
	 * metodo per inserire nel post i vari id
	 * @param postId id del post
	 * @param pointId id del point
	 * @param cityId id del comune
	 */
	public void setIds(String postId, String pointId, String cityId) {
		this.post.setPostId(postId);
		this.post.setPointId(pointId);
		this.post.setCityId(cityId);
	}
	
	/**
	 * metodo per inserire i dati nel post
	 * @param title titolo del post
	 * @param text testo del post
	 * @param multimediaData dati multimediali del post
	 */
	public void setData(String title, String text, List<String> multimediaData) {
		this.post.setTitle(title);
		this.post.setText(text);
		this.post.setMultimediaData(multimediaData);
	}
	
	/**
	 * metodo per inserire dettagli nel post
	 * @param author autore del post
	 * @param pos posizione del post
	 * @param ofCity se e' stato creato dal comune
	 * @param type tipo del post
	 */
	public void setDetails(String author, Position pos, boolean ofCity, PostType type) {
		this.post.setAuthor(author);
		this.post.setPos(pos);
		this.post.setOfCity(ofCity);
		this.post.setType(type);
	}
	
	/**
	 * metodo per inserire dettagli speciali nel post
	 * @param publ pubblicazione
	 * @param start momento di inizio
	 * @param end momento di fine
	 * @param pers persistenza
	 */
	public void setSpecialDetails(boolean publ, LocalDateTime start, LocalDateTime end, boolean pers) {
		this.post.setPublished(publ);
		this.post.setStartTime(start);
		this.post.setEndTime(end);
		this.post.setPersistence(pers);
	}
	
	/**
	 * metodo che verifica se un dato post e'
	 * stato correttamente compilato
	 * @return true se il post e' stato correttamente compilato
	 */
	public abstract boolean correctPost();
	
	/**
	 * metodo per l'emissione finale del post
	 * @return il post creato
	 */
	public Post createPost() {
		Post post = this.post;
		this.post = null;
		return post;
	}
	
}