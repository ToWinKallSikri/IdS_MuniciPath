package Synk.Api.Model.Post.Contribute;

import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Contribute {
	
	@Id
	private String id;
	private String author;
	private String contestId;
	@ElementCollection
    @Fetch(FetchMode.JOIN)
	private List<String> contribute;
	
	public Contribute(String id, String author, String contestId, List<String> contribute) {
		this.id = id;
		this.author = author;
		this.contestId = contestId;
		this.contribute = contribute;
	}
	
	public Contribute() {}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contribute other = (Contribute) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
}
