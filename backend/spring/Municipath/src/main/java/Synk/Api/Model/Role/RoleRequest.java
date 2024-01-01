package Synk.Api.Model.Role;

import java.util.Objects;

public class RoleRequest {
    private String requestId;
    private String cityId;
    private String username;

    public RoleRequest() {}

    public RoleRequest(String cityId, String username, String requestId) {
        super();
        this.cityId = cityId;
        this.username = username;
        this.requestId = requestId;
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

	@Override
	public int hashCode() {
		return Objects.hash(requestId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleRequest other = (RoleRequest) obj;
		return Objects.equals(requestId, other.requestId);
	}
    
    
    
}
