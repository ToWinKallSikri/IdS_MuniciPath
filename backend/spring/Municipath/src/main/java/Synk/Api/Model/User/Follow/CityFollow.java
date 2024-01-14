package Synk.Api.Model.User.Follow;

public class CityFollow {

    private String Id;
    private String cityId;
    private String username;

    public CityFollow(String Id, String cityId, String username) {
        this.Id = Id;
        this.cityId = cityId;
        this.username = username;
    }

    public CityFollow() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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




}
