package Synk.Api.Controller.Post;

import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;

public class ContestPostBuilder extends PostBuilder {

	@Override
	public boolean correctPost() {
		return this.post.getType() == PostType.CONTEST && checkTiming(this.post) && this.post.isPersistence();
	}
	
	/**
	 * Controlla che le variabili temporali siano corrette
	 * @param post post da controllare
	 * @return true se sono corrette per questo tipo di creator, false altrimenti
	 */
	private boolean checkTiming(Post post) {
		return this.post.getStartTime() == null && this.post.getEndTime() != null;
	}

}
