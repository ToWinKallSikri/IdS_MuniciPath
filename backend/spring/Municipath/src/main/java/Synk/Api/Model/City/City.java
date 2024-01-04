package Synk.Api.Model.City;
import java.util.Objects;

import Synk.Api.Model.Post.Position;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class City {

	/**
	 * Identificatore univoco della città
	 */
	@Id
    private String Id;
	/**
	 * Nome della città
	 */
	private String Name;
	/**
	 * Curatore della città
	 */
    private String curator;
	/**
	 * Codice postale della città
	 */
    private int cap;
	/**
	 * Posizione della città
	 */
    @Embedded
    private Position Pos;

	/**
	 * Costruttore della città
	 * @param id, identificatore univoco della città
	 * @param name, nome della città
	 * @param pos, posizione della città
	 * @param curator, curatore della città
	 * @param cap, codice postale della città
	 */
    public City(String id, String name, Position pos, String curator, int cap) {
        this.Id = id;
        this.Name = name;
        this.Pos = pos;
        this.curator = curator;
        this.cap = cap;
    }

	/**
	 * Costruttore vuoto della città, necessario per JPA
	 */
	public City() {}

	/**
	 * Ritorna l'identificatore univoco della città
	 * @return una stringa, che corrisponde all'id della città
	 */
	public String getId() {
		return Id;
	}

	/**
	 * Imposta l'identificatore univoco della città
	 * @param id, sarà l'id della città
	 */
	public void setId(String id) {
		Id = id;
	}

	/**
	 * Ritorna il nome della città
	 * @return una stringa, che corrisponde al nome della città
	 */
	public String getName() {
		return Name;
	}

	/**
	 * Imposta il nome della città
	 * @param name, sarà il nome della città
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * Ritorna il curatore della città
	 * @return una stringa, che corrisponde al curatore della città
	 */
	public String getCurator() {
		return curator;
	}

	/**
	 * Imposta il curatore della città
	 * @param curator, sarà il curatore della città
	 */
	public void setCurator(String curator) {
		this.curator = curator;
	}

	/**
	 * Ritorna il codice postale della città
	 * @return un intero, che corrisponde al codice postale della città
	 */
	public int getCap() {
		return cap;
	}

	/**
	 * Imposta il codice postale della città
	 * @param cap, sarà il codice postale della città
	 */
	public void setCap(int cap) {
		this.cap = cap;
	}

	/**
	 * Ritorna la posizione della città
	 * @return un oggetto, che corrisponde alla posizione della città
	 */
	public Position getPos() {
		return Pos;
	}

	/**
	 * Imposta la posizione della città
	 * @param pos, sarà la posizione della città
	 */
	public void setPos(Position pos) {
		Pos = pos;
	}

	/**
	 * Override del metodo hashCode
	 * @return un intero, che corrisponde all'hash dell'id della città
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Id);
	}

	/**
	 * Override del metodo equals
	 * @param obj, oggetto da confrontare
	 * @return un booleano, che indica se l'oggetto è uguale o meno
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		return Objects.equals(Id, other.Id);
	}
	
	

}
