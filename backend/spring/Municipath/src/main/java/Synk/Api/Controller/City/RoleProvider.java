package Synk.Api.Controller.City;

import Synk.Api.Model.City.Role.Role;

/**
 * interfaccia per una fonte di ruoli
 */
public interface RoleProvider {
	
	/**
	 * metodo per ricercare un ruolo
	 * @param username nome utente
	 * @param cityId id del comune
	 * @return ruolo che l'utente ha per quel comune
	 */
	public Role getRole(String username, String cityId);
	
}
