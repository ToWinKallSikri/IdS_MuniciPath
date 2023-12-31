package Synk.Api.Model.Post;

import java.util.List;

public class Contribute {
	
	private String author;
	private String contestId;
	private List<String> contribute;
	
	public Contribute(String author, String contestId, List<String> contribute) {
		this.author = author;
		this.contestId = contestId;
		this.contribute = contribute;
	}
	
	public Contribute() {}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContestId() {
		return contestId;
	}

	public void setContestId(String contestId) {
		this.contestId = contestId;
	}

	public List<String> getContribute() {
		return contribute;
	}

	public void setContribute(List<String> contribute) {
		this.contribute = contribute;
	}
	
}
