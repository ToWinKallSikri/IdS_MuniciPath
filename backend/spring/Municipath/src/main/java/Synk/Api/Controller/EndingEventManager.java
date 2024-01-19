package Synk.Api.Controller;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Post.PointHandler;

@Service
public class EndingEventManager {
	
	private PointHandler pointHandler;
    private GroupHandler groupHandler;
	
	public EndingEventManager(PointHandler pointHandler, GroupHandler groupHandler) {
		this.pointHandler = pointHandler;
		this.groupHandler = groupHandler;
		Executors.newScheduledThreadPool(1)
		.scheduleAtFixedRate(this::checkEnding, 0, 5, TimeUnit.MINUTES);
	}
	
	private void checkEnding() {
		this.pointHandler.checkEndingPosts();
		this.groupHandler.checkEndingGroups();
	}
	
}
