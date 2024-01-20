package Synk.Api.Controller.User;

import Synk.Api.Model.User.User;

/**
 * interfaccia per un fornitore di oggetti utente
 */
public interface UserProvider {
	
	/**
	 * metodo per ottenre un oggetto user
	 * @param username nome utente
	 * @return oggetto user
	 */
	public User getUser(String username);
	
}
