package Synk.Api.Controller.SavedContent;

import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Model.SavedContent.SavedContent;

import java.util.ArrayList;
import java.util.List;

public class SavedContentHandler {

    private MuniciPathMediator mediator;

    private List<SavedContent> savedContents;

    public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }

    public SavedContentHandler() {
        this.savedContents = new ArrayList<>();
    }

    public List<String> getPartecipants(String contentId){
        return savedContents.stream().
                filter(s -> s.getContentId().equals(contentId))
                .map(SavedContent::getUsername).toList();
    }

	public boolean saveSavedContent(String username, String contentId) {
        if (!(mediator.usernameExists(username) && mediator.contentExist(contentId)))
                return false;
        SavedContent s1 = new SavedContent(username, contentId);
        if(savedContents.stream().anyMatch(s -> s.getId().equals(s1.getId())))
            return false;
        return savedContents.add(s1);
	}

	public boolean removeSavedContent(String username, String contentId) {
        if (!(mediator.usernameExists(username) && mediator.contentExist(contentId)))
            return false;
        SavedContent s1 = new SavedContent(username, contentId);
        if(savedContents.stream().noneMatch(s -> s.getId().equals(s1.getId())))
            return false;
        return savedContents.remove(s1);
	}

	public List<String> getSavedContent(String username) {
        if(!(mediator.usernameExists(username)))
            return null;
		return savedContents.stream()
                .filter(s -> s.getUsername().equals(username))
                .map(SavedContent::getContentId).toList();
	}
	
}
