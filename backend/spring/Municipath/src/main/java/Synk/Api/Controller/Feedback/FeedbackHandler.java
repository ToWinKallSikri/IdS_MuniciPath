package Synk.Api.Controller.Feedback;

import java.util.List;

import Synk.Api.Model.Feedback.FeedbackRepository;
import Synk.Api.Model.Feedback.Score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Model.Feedback.Feedback;

@Service
public class FeedbackHandler {
	

	/**
	 * mediatore tra i vari handler
	 */
	private MuniciPathMediator mediator;

    @Autowired
	private FeedbackRepository feedbackRepository;

	/**
	 * imposta il mediator
	 * @param mediator mediatore da inserire
	 */
	public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }
	
	/**
	 * metodo per valutare un contenuto. una eventuale
	 * valutazione precedente dello stesso utente
	 * verra' sovrascritta
	 * @param username nome utente
	 * @param contentId contenuto da valutare
	 * @param vote voto
	 * @return true se la valutazione e' andata a buon
	 * fine, false altrimenti
	 */
	public boolean valute(String username, String contentId, int vote) {
		if(username == null || contentId == null)
			return false;
		if(vote < 1 || vote > 5)
			return false;
		if(!(this.mediator.usernameExists(username) && this.mediator.contentExist(contentId)))
			return false;
		String id = username + "." + contentId;
        this.feedbackRepository.findById(id).ifPresent(feedback -> this.feedbackRepository.delete(feedback));
        Feedback newFeedback = new Feedback(id, username, contentId, vote);
		this.feedbackRepository.save(newFeedback);
		return true;
	}
	
	/**
	 * metodo per ottenere il voto di un contenuto
	 * @param contentId id del contenuto
	 * @return voto del contenuto
	 */
	public Score getFeedback(String contentId) {
		if(contentId == null)
			return null;
		List<Feedback> list = this.feedbackRepository.findByContentId(contentId);
		if(list.isEmpty())
			return new Score(0, 0);
		float sum = list.stream().map(f -> f.getVote()).reduce((a,b) -> a + b).orElse(0f);
		return new Score(sum/list.size(), list.size());
	}
	
	/**
	 * metodo per rimuovere tutti i voti
	 * di un contenuto destinato alla eliminazione
	 * @param contentId id del contenuto
	 */
	public void removeAllFeedbackOf(String contentId) {
		if(contentId == null)
			return;
		List<Feedback> list =this.feedbackRepository.findByContentId(contentId);
		this.feedbackRepository.deleteAll(list);
	}
}
