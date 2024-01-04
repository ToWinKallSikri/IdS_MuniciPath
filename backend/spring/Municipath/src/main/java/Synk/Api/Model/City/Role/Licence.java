package Synk.Api.Model.City.Role;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Licence {

    /**
     * Identificatore univoco della licenza, composto da cityId e username
     */
	@Id
	private String id;
    /**
     * Identificatore univoco della città
     */
    private String cityId;
    /**
     * Username dell'utente a cui è stata assegnata la licenza
     */
    private String username;

    /**
     * Ruolo dell'utente a cui è stata assegnata la licenza
     */
	@Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Costruttore vuoto della classe, necessario per JPA
     */
    public Licence() {}

    /**
     * Costruttore della classe Licence
     * @param cityId, l'id della città
     * @param username, l'username dell'utente
     * @param role, il ruolo dell'utente
     */
    public Licence(String cityId, String username, Role role) {
    	this.id = cityId + "." + username;
        this.cityId = cityId;
        this.username = username;
        this.role = role;
    }

    /**
     * Metodo getter per ottenere l'id della licenza
     * @return una stringa, che corrisponde all'id della licenza
     */
    public String getId() {
		return id;
	}

    /**
     * Metodo setter, per impostare l'id della licenza
     * @param id, l'id della licenza
     */
	public void setId(String id) {
		this.id = id;
	}

    /**
     * Metodo getter per ottenere l'id della città
     * @return una stringa, che corrisponde all'id della città
     */
	public String getCityId() {
        return cityId;
    }

    /**
     * Metodo setter, per impostare l'id della città
     * @param cityId, l'id della città
     */
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     * Metodo getter per ottenere l'username dell'utente
     * @return una stringa, che corrisponde all'username dell'utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * Metodo setter, per impostare l'username dell'utente
     * @param username, l'username dell'utente
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Metodo getter per ottenere il ruolo dell'utente
     * @return un oggetto Role, che corrisponde al ruolo dell'utente
     */
    public Role getRole() {
        return role;
    }

    /**
     * Metodo setter, per impostare il ruolo dell'utente
     * @param role, il ruolo dell'utente
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Metodo per ottenere una stringa che rappresenta la licenza
     * @return una stringa, che rappresenta la licenza
     */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

    /**
     * Metodo per confrontare due licenze
     * @param obj, oggetto da confrontare
     * @return true se le due licenze sono uguali, false altrimenti
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Licence other = (Licence) obj;
		return Objects.equals(id, other.id);
	}
    
    

}
