package Synk.Api.Model.Feedback;

public class Feedback {
    private String id;
    private String username;
    private String contentId;
    private float vote;

    public Feedback(String Id, String username, String contentId, float vote) {
        this.id = Id;
        this.username = username;
        this.contentId = contentId;
        this.vote = vote;
    }

    public Feedback(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public float getVote() {
        return vote;
    }

    public void setVote(float vote) {
        this.vote = vote;
    }
}
