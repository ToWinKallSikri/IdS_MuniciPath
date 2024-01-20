package Synk.Api.Controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import Synk.Api.Model.Group.Group;
import Synk.Api.Model.Group.GroupRepository;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostRepository;
/**
 * Questa classe non è utile ai fini dell'applicazione
 * e serve solo per lo svolgimento dei test.
 */
@Service
public class ContentTimeModifier {
	
	public enum TimeType{
		PUBL,
		START,
		END
	}
	
	private PostRepository postRepo;
	private GroupRepository groupRepo;
	private IdentifierManager idManager;
	
	public ContentTimeModifier(PostRepository postRepo, GroupRepository groupRepo) {
		this.postRepo = postRepo;
		this.groupRepo = groupRepo;
		this.idManager = new IdentifierManager();
	}
	
	public boolean modifyTime (String contentId, int time, ChronoUnit unit, TimeType op) {
		if(this.idManager.isGroup(contentId))
			return modifyGroupTime(contentId, time, unit, op);
		return modifyPostTime(contentId, time, unit, op);
	}

	private boolean modifyGroupTime(String contentId, int time, ChronoUnit unit, TimeType op) {
		if(!this.groupRepo.existsById(contentId))
			return false;
		Group content = this.groupRepo.findById(contentId).get();
		LocalDateTime newTime = makeNewTime(content.getPublicationTime(), time, unit);
		if(op == TimeType.START)
			content.setStartTime(newTime);
		if(op == TimeType.END)
			content.setEndTime(newTime);
		if(op == TimeType.PUBL)
			content.setPublicationTime(newTime);
		this.groupRepo.save(content);
		return true;
	}

	private boolean modifyPostTime(String contentId, int time, ChronoUnit unit, TimeType op) {
		if(!this.postRepo.existsById(contentId))
			return false;
		Post content = this.postRepo.findById(contentId).get();
		LocalDateTime newTime = makeNewTime(content.getPublicationTime(), time, unit);
		if(op == TimeType.START)
			content.setStartTime(newTime);
		if(op == TimeType.END)
			content.setEndTime(newTime);
		if(op == TimeType.PUBL)
			content.setPublicationTime(newTime);
		this.postRepo.save(content);
		return true;
	}
	
	private LocalDateTime makeNewTime(LocalDateTime publicationTime, int time, ChronoUnit unit) {
		if(time > 0) {
			return publicationTime.plus(time, unit);
		} else if(time < 0) {
			return publicationTime.minus(-time, unit);
		} else {
			return publicationTime;
		}
	}
	
}
