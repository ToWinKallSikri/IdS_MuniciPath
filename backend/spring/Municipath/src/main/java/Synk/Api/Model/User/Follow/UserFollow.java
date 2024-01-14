package Synk.Api.Model.User.Follow;

public class UserFollow {

    private String Id;
    private String username;
    private String contributor;

    public UserFollow(String Id, String username, String contributor) {
        this.Id = Id;
        this.username = username;
        this.contributor = contributor;
    }

    public UserFollow(){
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }
}
