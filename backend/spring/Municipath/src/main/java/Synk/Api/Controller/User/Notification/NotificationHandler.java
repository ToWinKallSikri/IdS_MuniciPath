package Synk.Api.Controller.User.Notification;

import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.User.Notification.Notification;
import org.joda.time.DateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationHandler {

    private List<Notification> notifies;

    private IdentifierManager idManager;

    private UserHandler userHandler;

    public NotificationHandler() {
        this.notifies = new ArrayList<>();
        this.idManager = new IdentifierManager();
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
