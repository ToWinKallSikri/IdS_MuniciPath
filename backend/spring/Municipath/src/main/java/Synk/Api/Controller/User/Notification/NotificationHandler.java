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
}
