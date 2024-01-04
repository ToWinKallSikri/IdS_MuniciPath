package Synk.Api.Model.City.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;
@Entity
public class RoleRequest {
    /**
     * Identificatore univoco della richiesta
     */
    @Id
    private String requestId;
    /**
     * Identificatore univoco della città
     */
    private String cityId;
    /**
     * Username dell'utente che ha fatto la richiesta di promozione di ruolo
     */
    private String username;

    /**
     * Costruttore vuoto della classe, necessario per la JPA
     */
    public RoleRequest() {}

    /**
     * Costruttore della classe
     * @param cityId, id della città
     * @param username, username dell'utente che ha fatto la richiesta
     * Ogni richiesta ha un suo identificatore univoco, che è dato dalla concatenazione di cityId e username
     */
    public RoleRequest(String cityId, String username) {
        this.cityId = cityId;
        this.username = username;
        this.requestId =cityId + "." + username;
    }

    /**
     * Metodo che restituisce l'id della città
     * @return una stringa che rappresenta l'id della città
     */
    public String getCityId() {
        return cityId;
    }

    /**
     * Metodo che setta l'id della città
     * @param cityId, id della città
     */
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     * Metodo che restituisce l'username dell'utente che ha fatto la richiesta
     * @return una stringa che rappresenta l'username dell'utente che ha fatto la richiesta
     */
    public String getUsername() {
        return username;
    }

    /**
     * Metodo che setta l'username dell'utente che ha fatto la richiesta
     * @param username, username dell'utente che ha fatto la richiesta
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Metodo che restituisce l'id della richiesta
     * @return una stringa che rappresenta l'id della richiesta
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Metodo che setta l'id della richiesta
     * @param requestId, id della richiesta
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Metodo che fa l'hash del requestId
     * @return un intero che rappresenta l'hash del requestId
     */
	@Override
	public int hashCode() {
		return Objects.hash(requestId);
	}

    /**
     * Metodo che controlla se due oggetti sono uguali
     * @param obj, oggetto da confrontare
     * @return un booleano che indica se i due oggetti sono uguali
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleRequest other = (RoleRequest) obj;
		return Objects.equals(requestId, other.requestId);
	}
    
    
    
}
