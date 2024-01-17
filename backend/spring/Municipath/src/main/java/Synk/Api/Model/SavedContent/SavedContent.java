package Synk.Api.Model.SavedContent;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SavedContent {

    @Id
	private String Id;
    private String username;
    private String contentId;

    public SavedContent(String username, String contentId) {
        this.Id = username + "." + contentId;
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
