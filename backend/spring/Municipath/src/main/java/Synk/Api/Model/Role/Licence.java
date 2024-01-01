package Synk.Api.Model.Role;

public class Licence {
    private String cityId;
    private String username;
    private Role role;

    public Licence() {}
    public Licence(String cityId, String username, Role role) {
        super();
        this.cityId = cityId;
        this.username = username;
        this.role = role;
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

}
