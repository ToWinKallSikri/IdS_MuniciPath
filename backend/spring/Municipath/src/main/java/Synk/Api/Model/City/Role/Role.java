package Synk.Api.Model.City.Role;

public enum Role {

	CURATOR,
	MODERATOR,
	CONTR_AUTH,
	CONTR_NOT_AUTH,
	TOURIST,
	LIMITED;
	
	
	public static Role safeValueOf(String data) {
		try {
			return valueOf(data.toUpperCase());
		} catch(Exception e) {
			return Role.TOURIST;
		}
	}
}
