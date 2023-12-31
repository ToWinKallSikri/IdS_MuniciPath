package Synk.Api.Model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Point {
	
	@Id
	private String pointId;
	@Embedded
    private Position pos;
    private String cityId;
    @Transient
	private List<Post> posts;
	private int idCount;
    
    public Point() {
    	this.posts = new ArrayList<>();
		this.idCount = 0;
    }
    
    public Point(String id, Position pos, String cityId) {
    	this(id, pos, cityId, new ArrayList<>());
    }
    
    public Point(String id, Position pos, String cityId, ArrayList<Post> posts) {
    	this.pointId = id;
		this.pos = pos;
		this.cityId = cityId;
		this.posts = posts;
		this.idCount = 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		return Objects.equals(pos, other.pos);
	}
	
	

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public int getIdCount() {
		return idCount;
	}

	public void setIdCount(int idCount) {
		this.idCount = idCount;
	}
    
	public String getNewPostId() {
		return this.pointId + "." + this.idCount++;
	}

}
