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
    
    /**
     * metodo per ottenere la lista degli utenti che
     * hanno salvato un dato contenuto
     * @param contentId id del contenuto
     * @return lista di utenti
     */
    public List<String> getPartecipants(String contentId){
    	if(contentId == null)
    		return null;
        return savedContentRepository.findByContentId(contentId)
                .stream().map(SavedContent::getUsername).toList();
    }
    
    /**
     * metodo per salvare un contenuto
     * @param username nome utente
     * @param contentId id del contenuto
     * @return true se il salvataggio e' andato
     * a buon fine, false altrimenti
     */
	public boolean saveContent(String username, String contentId) {
		if(username == null || contentId == null)
    		return false;
        if (!(mediator.usernameExists(username) && mediator.contentExist(contentId)))
                return false;
        SavedContent s1 = new SavedContent(username, contentId);
        if(savedContentRepository.existsById(s1.getId()))
            return false;
        savedContentRepository.save(s1);
        return true;
	}

	/**
	 * metodo per rimuovere un contenuto dalla
	 * lista dei salvati
	 * @param username nome utente
	 * @param contentId id del contenuto
	 * @return true se la rimozione e' andata
	 * a buon fine, false altrimenti
	 */
	public boolean removeSavedContent(String username, String contentId) {
		if(username == null || contentId == null)
    		return false;
        if (!(mediator.usernameExists(username) && mediator.contentExist(contentId)))
            return false;
        SavedContent s1 = new SavedContent(username, contentId);
        if(!savedContentRepository.existsById(s1.getId()))
            return false;
        savedContentRepository.delete(s1);
        return true;
	}

	/**
	 * metodo per ottenre i contenuti salvati
	 * @param username nome utente
	 * @return contenuti salvati
	 */
	public List<String> getSavedContent(String username) {
		if(username == null)
    		return null;
        if(!(mediator.usernameExists(username)))
            return null;
		return savedContentRepository.findByUsername(username).stream()
                .map(SavedContent::getContentId).toList();
	}
	
	/**
	 * meotodo per rimuovere tutti i contenuti salvati
	 * da un singolo utente
	 * @param username nome utente
	 */
	public void removeAllFromUser(String username) {
		if(username == null)
    		return;
		List<SavedContent> list = savedContentRepository.findByUsername(username);
		this.savedContentRepository.deleteAll(list);
	}
	
	/**
	 * metodo per rimuovere un contenuto da
	 * tutte le liste dei contenuti salvati
	 * @param contentId contenuto da rimuovere
	 */
	public void removeAllFromContent(String contentId) {
		if(contentId == null)
    		return;
		List<SavedContent> list = savedContentRepository.findByContentId(contentId);
		this.savedContentRepository.deleteAll(list);
	}
	
}
