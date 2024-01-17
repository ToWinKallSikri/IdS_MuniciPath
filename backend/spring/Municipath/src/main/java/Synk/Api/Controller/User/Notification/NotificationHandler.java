package Synk.Api.Controller.User.Notification;

import Synk.Api.Model.User.Notification.Notification;
import Synk.Api.Model.User.Notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationHandler {

    @Autowired
    private NotificationRepository notificationRepository;

	public void notify(String author, String message, String contentId, String reciver) {
        Notification n1 = new Notification(author, message, contentId, reciver);
        notificationRepository.save(n1);
	}
	
	public void notifyEvent(String author, String message, String contentId, List<String> receivers) {
        List<String> l1 = receivers;
        for (String s : l1) {
            notify(author, message, contentId, s);
        }
    }
	
	public List<Notification> getMyMessages(String username){
		return this.notificationRepository.findByReceiver(username);
	}
	
	public Notification getMyMessage(String username, String id) {
		Notification msg = notificationRepository.findById(id).orElse(null);
		if(msg != null) {
			msg.setRead(true);
            notificationRepository.save(msg);
		}
		return msg;
	}


	public void deleteUser(String username) {
		this.notificationRepository.deleteAll(getMyMessages(username));
	}
}
