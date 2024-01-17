package Synk.Api.Model.Feedback;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeedbackRepository extends CrudRepository<Feedback, String> {

    List<Feedback> findByContentId(String contentId);
}
