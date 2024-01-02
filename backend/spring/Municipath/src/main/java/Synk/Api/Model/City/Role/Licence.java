package Synk.Api.Model.City.Role;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Licence {
	
	@Id
	private String id;
    private String cityId;
    private String username;
	@Enumerated(EnumType.STRING)
    private Role role;

    public Licence() {}
    
    public Licence(String cityId, String username, Role role) {
    	this.id = cityId + "." + username;
        this.cityId = cityId;
        this.username = username;
        this.role = role;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
		Licence other = (Licence) obj;
		return Objects.equals(id, other.id);
	}
    
    

}
