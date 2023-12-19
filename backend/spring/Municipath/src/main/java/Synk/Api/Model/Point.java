package Synk.Api.Model;

import java.util.ArrayList;
import java.util.Objects;

public class Point {
	
	private String id;
    private Position pos;
    private String cityId;
	private ArrayList<Post> posts;
    
    public Point() {}
    
    public Point(String id, Position pos, String cityId) {
		super();
		this.id = id;
		this.pos = pos;
		this.cityId = cityId;
		this.posts = new ArrayList<>();
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
		Point other = (Point) obj;
		return Objects.equals(id, other.id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public ArrayList<Post> getPosts() {
		return posts;
	}

	public void setPosts(ArrayList<Post> posts) {
		this.posts = posts;
	}
    


}
