package Synk.Api.Controller.SavedContent;

import Synk.Api.Controller.MuniciPathMediator;

import Synk.Api.Model.SavedContent.SavedContent;

import java.util.List;

import Synk.Api.Model.SavedContent.SavedContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SavedContentHandler {

    private MuniciPathMediator mediator;

    @Autowired
    private SavedContentRepository savedContentRepository;

    public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }

    public List<String> getPartecipants(String contentId){
        return savedContentRepository.findByContentId(contentId)
                .stream().map(SavedContent::getUsername).toList();
    }

	public boolean saveSavedContent(String username, String contentId) {
        if (!(mediator.usernameExists(username) && mediator.contentExist(contentId)))
                return false;
        SavedContent s1 = new SavedContent(username, contentId);
        if(savedContentRepository.existsById(s1.getId()))
            return false;
        savedContentRepository.save(s1);
        return true;
	}

	public boolean removeSavedContent(String username, String contentId) {
        if (!(mediator.usernameExists(username) && mediator.contentExist(contentId)))
            return false;
        SavedContent s1 = new SavedContent(username, contentId);
        if(!savedContentRepository.existsById(s1.getId()))
            return false;
        savedContentRepository.delete(s1);
        return true;
	}

	public List<String> getSavedContent(String username) {
        if(!(mediator.usernameExists(username)))
            return null;
		return savedContentRepository.findByUsername(username).stream()
                .map(SavedContent::getContentId).toList();
	}
	
}
