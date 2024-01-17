package Synk.Api.Controller.User.Notification;

import Synk.Api.Model.User.Notification.Notification;
import java.util.ArrayList;
import java.util.List;

public class NotificationHandler {

    private List<Notification> notifies;

    public NotificationHandler() {
        this.notifies = new ArrayList<>();
    }

	
	public void notify(String author, String message, String contentId, String reciver) {
        Notification n1 = new Notification(author, message, contentId, reciver);
        notifies.add(n1);
	}
	
	public void notifyEvent(String author, String message, String contentId, List<String> receivers) {
        List<String> l1 = receivers;
        for (String s : l1) {
            notify(author, message, contentId, s);
        }
    }
	
	public List<Notification> getMyMessages(String username){
		return this.notifies.stream()
				.filter(n-> n.getReceiver().equals(username)).toList();
	}
	
	public Notification getMyMessage(String username, String id) {
		Notification msg = this.getMyMessages(username).stream()
				.filter(n -> n.getId().equals(id))
				.findFirst().orElse(null);
		if(msg != null) {
			msg.setRead(true);
		}
		return msg;
	}


	public void deleteUser(String username) {
		this.notifies.removeAll(getMyMessages(username));
	}
}
