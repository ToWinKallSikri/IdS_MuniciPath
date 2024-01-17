package Synk.Api.Controller.Post;

import java.time.LocalDateTime;

public class PostValidator {
	
	/**
	 * metodo che verifica se un dato 
	 * post sono coerenti tra di loro
	 * @param builder
	 * @param publ
	 * @param start
	 * @param end
	 * @param pers
	 * @return true se il post e' stato correttamente compilato
	 */
	public boolean correctPost(PostCreator builder, boolean publ, LocalDateTime start, 
														LocalDateTime end, boolean pers) {
		builder.initializePost();
		builder.setSpecialDetails(publ, start, end, pers);
		return builder.correctPost();
	}
	
}
