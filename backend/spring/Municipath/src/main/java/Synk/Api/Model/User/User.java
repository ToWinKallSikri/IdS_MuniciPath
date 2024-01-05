package Synk.Api.Model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tourist")
public class User {

	/**
	 * Username dell'utente
	 */
	@Id
    private String username;

	/**
	 * Password dell'utente
	 */
    private String password;

	/**
	 * Booleano che indica se l'utente è un gestore della piattaforma o meno
	 */
    private boolean isManager;

	/**
	 * Identificatore univoco della città in cui l'utente è un curatore
	 */
    private String cityId;

	/**
	 * Booleano che indica se l'utente è stato convalidato o meno
	 */
    private boolean convalidated;

	/**
	 * Costruttore della classe User
	 * @param username, l'username dell'utente
	 * @param password, la password dell'utente
	 * @param isManager, booleano che indica se l'utente è un gestore della piattaforma o meno
	 * @param convalidated, booleano che indica se l'utente è stato convalidato o meno
	 */
	public User(String username, String password, boolean isManager, boolean convalidated) {
		this.username = username;
		this.password = password;
		this.isManager = isManager;
		this.convalidated = convalidated;
	}

	/**
	 * Costruttore vuoto della classe, necessario per JPA
	 */
	public User() {}

	/**
	 * Metodo getter per ottenere l'username dell'utente
	 * @return una stringa, che corrisponde all'username dell'utente
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Metodo setter per impostare l'username dell'utente
	 * @param username, l'username dell'utente
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Metodo getter per ottenere la password dell'utente
	 * @return una stringa, che corrisponde alla password dell'utente
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Metodo setter per impostare la password dell'utente
	 * @param password, la password dell'utente
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Metodo getter per sapere se l'utente è un gestore della piattaforma o meno
	 * @return un booleano, che indica se l'utente è un gestore della piattaforma o meno
	 */
	public boolean isManager() {
		return isManager;
	}

	/**
	 * Metodo setter per impostare se l'utente è un gestore della piattaforma o meno
	 * @param isManager, booleano che indica se l'utente è un gestore della piattaforma o meno
	 */
	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}

	/**
	 * Metodo getter per ottenere l'id della città in cui l'utente è un curatore
	 * @return una stringa, che corrisponde all'id della città in cui l'utente è un curatore
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * Metodo setter per impostare l'id della città in cui l'utente è un curatore
	 * @param cityId, l'id della città in cui l'utente è un curatore
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * Metodo che restituisce un booleano che indica se l'utente è un curatore o meno
	 * @return un booleano che indica se l'utente è un curatore o meno
	 */
	public boolean isCurator() {
		return this.cityId != null;
	}

	/**
	 * Metodo getter per sapere se l'utente è stato convalidato o meno
	 * @return un booleano, che indica se l'utente è stato convalidato o meno
	 */
	public boolean isConvalidated() {
		return convalidated;
	}

	/**
	 * Metodo setter per impostare se l'utente è stato convalidato o meno
	 * @param convalidated, booleano che indica se l'utente è stato convalidato o meno
	 */
	public void setConvalidated(boolean convalidated) {
		this.convalidated = convalidated;
	}
	
}
