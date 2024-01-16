package Synk.Api.Controller.Feedback;

import java.util.ArrayList;
import java.util.List;

import Synk.Api.Model.Feedback.Feedback;

public class FeedbackHandler {
	
	private List<Feedback> feedbacks;
	
	public FeedbackHandler() {
		this.feedbacks = new ArrayList<>();
	}

	public boolean valute(String username, String contentId, int vote) {
		if(vote < 1 || vote > 5)
			return false;
		String id = username + "." + contentId;
		Feedback feedback = this.feedbacks.stream()
				.filter(f -> f.getId().equals(id))
				.findFirst().orElse(null);
		if(feedback != null)
			this.feedbacks.remove(feedback);
		Feedback newFeedback = new Feedback(id, username, contentId, vote);
		this.feedbacks.add(newFeedback);
		return true;
	}
	
	public float getFeedback(String contentId) {
		List<Feedback> list = this.feedbacks.stream()
				.filter(f -> f.getContentId().equals(contentId)).toList();
		float sum = list.stream().map(f -> f.getVote()).reduce((a,b) -> a + b).orElse(0f);
		return sum == 0f ? 0f : sum/list.size();
	}
	
	
}
