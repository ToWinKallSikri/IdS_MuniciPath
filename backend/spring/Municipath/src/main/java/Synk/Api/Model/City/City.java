package Synk.Api.Model.City;




import java.util.Objects;

import Synk.Api.Model.Post.Position;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class City {
	
	@Id
    private String Id;
	private String Name;
    private String curator;
    private int cap;
    @Embedded
    private Position Pos;

    public City(String id, String name, Position pos, String curator, int cap) {
        this.Id = id;
        this.Name = name;
        this.Pos = pos;
        this.curator = curator;
        this.cap = cap;
    }
    
    public City() {}
    
    public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getCurator() {
		return curator;
	}

	public void setCurator(String curator) {
		this.curator = curator;
	}

	public int getCap() {
		return cap;
	}

	public void setCap(int cap) {
		this.cap = cap;
	}

	public Position getPos() {
		return Pos;
	}

	public void setPos(Position pos) {
		Pos = pos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		return Objects.equals(Id, other.Id);
	}
	
	

}
