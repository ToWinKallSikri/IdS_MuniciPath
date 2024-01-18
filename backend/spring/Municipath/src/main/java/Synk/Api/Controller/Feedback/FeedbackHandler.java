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
	
	public Score getFeedback(String contentId) {
		if(contentId == null)
			return null;
		List<Feedback> list = this.feedbackRepository.findByContentId(contentId);
		float sum = list.stream().map(f -> f.getVote()).reduce((a,b) -> a + b).orElse(0f);
		return sum == 0f ? null : new Score(sum/list.size(), list.size());
	}
	
	public void removeAllFeedbackOf(String contentId) {
		if(contentId == null)
			return;
		List<Feedback> list =this.feedbackRepository.findByContentId(contentId);
		this.feedbackRepository.deleteAll(list);
	}
}
