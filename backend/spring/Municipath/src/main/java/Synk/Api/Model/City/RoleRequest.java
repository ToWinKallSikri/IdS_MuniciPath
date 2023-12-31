package Synk.Api.Model.City;

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

}
