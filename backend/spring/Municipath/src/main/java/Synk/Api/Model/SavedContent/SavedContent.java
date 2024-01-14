package Synk.Api.Model.SavedContent;

public class SavedContent {
	private String Id;
    private String username;
    private String contentId;

    public SavedContent(String Id, String username, String contentId) {
        this.Id = Id;
        this.username = username;
        this.contentId = contentId;
    }

    public SavedContent(){
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

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
