package Synk.Api.Controller.Post;

import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;

public class NormalPostBuilder extends PostBuilder {

	@Override
	public boolean correctPost() {
		return isNormalType(this.post.getType()) && checkTiming(this.post) && this.post.isPersistence();
	}
	
	/**
	 * Controlla che le variabili temporali siano corrette
	 * @param post post da controllare
	 * @return true se sono corrette per questo tipo di creator, false altrimenti
	 */
	private boolean checkTiming(Post post) {
		return this.post.getStartTime() == null && this.post.getEndTime() == null;
	}
	
	/**
	 * controlla il tipo per assicurarsi che non sia un tipo speciale
	 * @param type tipo da controllare
	 * @return true se non e' un tipo speciale, false altrimenti.
	 */
	private boolean isNormalType(PostType type) {
		return type != PostType.CONTEST && type != PostType.EVENT;
	}
	
}
