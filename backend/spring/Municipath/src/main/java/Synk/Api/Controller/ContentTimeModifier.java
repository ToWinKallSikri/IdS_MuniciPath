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
	
	private PostRepository postRepo;
	private GroupRepository groupRepo;
	private IdentifierManager idManager;
	
	public ContentTimeModifier(PostRepository postRepo, GroupRepository groupRepo) {
		this.postRepo = postRepo;
		this.groupRepo = groupRepo;
		this.idManager = new IdentifierManager();
	}
	
	public boolean modifyStartTime (String contentId, int time, ChronoUnit unit) {
		if(this.idManager.isGroup(contentId))
			return modifyGroupStartTime(contentId, time, unit);
		return modifyPostStartTime(contentId, time, unit);
	}

	private boolean modifyGroupStartTime(String contentId, int time, ChronoUnit unit) {
		if(!this.groupRepo.existsById(contentId))
			return false;
		Group group = this.groupRepo.findById(contentId).get();
		LocalDateTime newTime = makeNewTime(group.getPublicationTime(), time, unit);
		group.setStartTime(newTime);
		this.groupRepo.save(group);
		return true;
	}

	private boolean modifyPostStartTime(String contentId, int time, ChronoUnit unit) {
		if(!this.postRepo.existsById(contentId))
			return false;
		Post post = this.postRepo.findById(contentId).get();
		LocalDateTime newTime = makeNewTime(post.getPublicationTime(), time, unit);
		post.setStartTime(newTime);
		this.postRepo.save(post);
		return true;
	}
	
	public boolean modifyEndTime (String contentId, int time, ChronoUnit unit) {
		if(this.idManager.isGroup(contentId))
			return modifyGroupEndTime(contentId, time, unit);
		return modifyPostEndTime(contentId, time, unit);
	}

	private boolean modifyGroupEndTime(String contentId, int time, ChronoUnit unit) {
		if(!this.groupRepo.existsById(contentId))
			return false;
		Group group = this.groupRepo.findById(contentId).get();
		LocalDateTime newTime = makeNewTime(group.getPublicationTime(), time, unit);
		group.setEndTime(newTime);
		this.groupRepo.save(group);
		return true;
	}

	private boolean modifyPostEndTime(String contentId, int time, ChronoUnit unit) {
		if(!this.postRepo.existsById(contentId))
			return false;
		Post post = this.postRepo.findById(contentId).get();
		LocalDateTime newTime = makeNewTime(post.getPublicationTime(), time, unit);
		post.setEndTime(newTime);
		this.postRepo.save(post);
		return true;
	}
	
	public boolean modifyPublicationTime (String contentId, int time, ChronoUnit unit) {
		if(this.idManager.isGroup(contentId))
			return modifyGroupPublicationTime(contentId, time, unit);
		return modifyPostPublicationTime(contentId, time, unit);
	}

	private boolean modifyGroupPublicationTime(String contentId, int time, ChronoUnit unit) {
		if(!this.groupRepo.existsById(contentId))
			return false;
		Group group = this.groupRepo.findById(contentId).get();
		LocalDateTime newTime = makeNewTime(group.getPublicationTime(), time, unit);
		group.setPublicationTime(newTime);
		this.groupRepo.save(group);
		return true;
	}

	private boolean modifyPostPublicationTime(String contentId, int time, ChronoUnit unit) {
		if(!this.postRepo.existsById(contentId))
			return false;
		Post post = this.postRepo.findById(contentId).get();
		LocalDateTime newTime = makeNewTime(post.getPublicationTime(), time, unit);
		post.setPublicationTime(newTime);
		this.postRepo.save(post);
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
	
	public enum TimeType{
		PUBL,
		START,
		END
	}
	
}
