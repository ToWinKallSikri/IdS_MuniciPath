package Synk.Api.Model.Pending;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import Synk.Api.Model.Post.PostType;
import Synk.Api.View.Model.ProtoGroup;
import Synk.Api.View.Model.ProtoPost;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class PendingRequest {
	/**
	 * Stringa che corrisponde all' Id della richiesta
	 */
	@Id
	private String id;
	/**
	 * Stringa che corrisponde al titolo della richiesta
	 */
	private String title;
	/**
	 * Stringa che corrisponde al testo della richiesta
	 */
	private String text;

	/**
	 * Booleano che indica se la richiesta in pending è per un post/gruppo nuovo o per l'aggiornamento
	 * di uno già esistente
	 */
	private boolean isNew;

	/**
	 * Booleano che indica se il gruppo messo in pending è un itinerario o meno
	 */
	private boolean sorted;

	/**
	 * Booleano che indica se il post/gruppo messo in pending è persistente o meno  dopo la scadenza
	 */
	private boolean persistence;

	/**
	 * Enum che indica il tipo di post messo in pending
	 */
	@Enumerated(EnumType.STRING)
	private PostType type;

	/**
	 * Contenuti multimediali (Lista di ID di posts  nel caso dei gruppi)  del post/gruppo messo in pending
	 */
    @ElementCollection
    @Fetch(FetchMode.JOIN)
	private List<String> data;

	/**
	 * Data di inizio del post/gruppo messo in pending
	 */
	private LocalDateTime startTime;

	/**
	 * Data di fine del post/gruppo messo in pending
	 */
	private LocalDateTime endTime;

	/**
	 * Costruttore della classe PendingRequest
	 * @param id stringa che corrisponde all' Id della richiesta
	 */
	public PendingRequest(String id) {
		this.id = id;
		this.isNew = true;
	}

	/**
	 * Costruttore della richiesta in caso di un gruppo
	 * @param id stringa che corrisponde all' Id della richiesta (corrisponde all' Id del gruppo)
	 * @param data dati del group
	 */
	public PendingRequest(String id, ProtoGroup data) {
		this.id = id;
		this.title = data.getTitle();
		this.isNew = false;
		this.sorted = data.isSorted();
		this.persistence = data.isPersistence();
		this.data = data.getPosts();
		this.startTime = data.getStartTime();
		this.endTime = data.getEndTime();
	}

	/**
	 * Costruttore della richiesta in caso di un post
	 * @param id stringa che corrisponde all' Id della richiesta (corrisponde all' Id del post)
	 * @param data dati del post
	 */
	public PendingRequest(String id , ProtoPost data) {
		this.id = id;
		this.title = data.getTitle();
		this.text = data.getText();
		this.isNew = false;
		this.persistence = data.isPersistence();
		this.type = data.getType();
		this.data = data.getMultimediaData();
		this.startTime = data.getStartTime();
		this.endTime = data.getEndTime();
	}

	/**
	 * Costruttore vuoto della classe PendingRequest  necessario per la JPA
	 */
	public PendingRequest() {}


	/**
	 * Metodo che ritorna l' Id della richiesta
	 * @return una stringa che corrisponde all' Id della richiesta
	 */
	public String getId() {
		return id;
	}

	/**
	 * Metodo che setta l' Id della richiesta
	 * @param id stringa che corrisponde all' Id della richiesta
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Metodo che ritorna il titolo della richiesta
	 * @return una stringa che corrisponde al titolo della richiesta
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Metodo che setta il titolo della richiesta
	 * @param title  stringa che corrisponde al titolo della richiesta
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Metodo che ritorna il testo della richiesta
	 * @return una stringa che corrisponde al testo della richiesta
	 */
	public String getText() {
		return text;
	}

	/**
	 * Metodo che setta il testo della richiesta
	 * @param text  stringa che corrisponde al testo della richiesta
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Metodo che ritorna un booleano che indica se la richiesta in pending è per un post/gruppo nuovo o per
	 * l'aggiornamento di uno già esistente
	 * @return un booleano che indica se la richiesta in pending è per un post/gruppo nuovo o meno
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * Metodo che setta un booleano che indica se la richiesta in pending è per un post/gruppo nuovo o per
	 * l'aggiornamento di uno già esistente
	 * @param isNew  booleano che indica se la richiesta in pending è per un post/gruppo nuovo o meno
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * Metodo che ritorna un booleano che indica se il gruppo messo in pending è un itinerario o meno
	 * @return un booleano che indica se il gruppo messo in pending è un itinerario o meno
	 */
	public boolean isSorted() {
		return sorted;
	}

	/**
	 * Metodo che setta un booleano che indica se il gruppo messo in pending è un itinerario o meno
	 * @param sorted  booleano che indica se il gruppo messo in pending è un itinerario o meno
	 */
	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	/**
	 * Metodo che ritorna un booleano che indica se il post/gruppo messo in pending è persistente o meno  dopo la scadenza
	 * @return un booleano che indica se il post/gruppo messo in pending è persistente o meno  dopo la scadenza
	 */
	public boolean isPersistence() {
		return persistence;
	}

	/**
	 * Metodo che setta un booleano che indica se il post/gruppo messo in pending è persistente o meno  dopo la scadenza
	 * @param persistence  booleano che indica se il post/gruppo messo in pending è persistente o meno  dopo la scadenza
	 */
	public void setPersistence(boolean persistence) {
		this.persistence = persistence;
	}

	/**
	 * Metodo che ritorna un enum che indica il tipo di post messo in pending
	 * @return un enum che indica il tipo di post messo in pending
	 */
	public PostType getType() {
		return type;
	}

	/**
	 * Metodo che setta un enum che indica il tipo di post messo in pending
	 * @param type  enum che indica il tipo di post messo in pending
	 */
	public void setType(PostType type) {
		this.type = type;
	}

	/**
	 * Metodo che ritorna i contenuti multimediali (Lista di ID di posts  nel caso dei gruppi) 
	 * del post/gruppo messo in pending
	 * @return una lista di stringhe che corrisponde ai contenuti multimediali (Lista di ID di posts  nel caso dei gruppi) 
	 * del post/gruppo messo in pending
	 */
	public List<String> getData() {
		return data;
	}

	/**
	 * Metodo che setta i contenuti multimediali (Lista di ID di posts  nel caso dei gruppi) 
	 * del post/gruppo messo in pending
	 * @param data  lista di stringhe che corrisponde ai contenuti multimediali (Lista di ID di posts  nel caso dei gruppi) 
	 * del post/gruppo messo in pending
	 */
	public void setData(List<String> data) {
		this.data = data;
	}

	/**
	 * Metodo che ritorna la data di inizio del post/gruppo messo in pending
	 * @return un oggetto LocalDateTime che corrisponde alla data di inizio del post/gruppo messo in pending
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Metodo che setta la data di inizio del post/gruppo messo in pending
	 * @param start  LocalDateTime che corrisponde alla data di inizio del post/gruppo messo in pending
	 */
	public void setStartTime(LocalDateTime start) {
		this.startTime = start;
	}

	/**
	 * Metodo che ritorna la data di fine del post/gruppo messo in pending
	 * @return un oggetto LocalDateTime che corrisponde alla data di fine del post/gruppo messo in pending
	 */
	public LocalDateTime getEndTime() {
		return endTime;
	}

	/**
	 * Metodo che setta la data di fine del post/gruppo messo in pending
	 * @param end  LocalDateTime che corrisponde alla data di fine del post/gruppo messo in pending
	 */
	public void setEndTime(LocalDateTime end) {
		this.endTime = end;
	}
	
	
	
	
}
