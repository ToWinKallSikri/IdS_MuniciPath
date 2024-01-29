package Synk.Api.Model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;


//@JoinColumn(name = "pointId", referencedColumnName = "pointId")

@Entity
public class Point {
	
	/**
	 * parametri della classe
	 */
	@Id
	private String pointId;
	@Embedded
    private Position pos;
    private String cityId;
    @Transient
    private List<Post> posts;
	private int idCount;
    
	/**
	 * costruttore vuoto del Point, per le logiche jpa
	 */
    public Point() {
    	this.posts = new ArrayList<>();
		this.idCount = 0;
    }
    
    /**
     * costruttore con paramentri del Point,
     * per le logiche del model.
     * accetta anche una lista di Post.
     * @param id id del punto
     * @param pos posizione del punto
     * @param cityId id della citta'
     * @param posts Posts da inserire
     */
    public Point(Position pos, String cityId) {
    	this.pointId = cityId+"."+pos;
		this.pos = pos;
		this.cityId = cityId;
		this.posts = new ArrayList<>();
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
