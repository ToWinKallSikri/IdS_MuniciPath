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
    
    /**
     * metodo per notificare un dato contenuto
     * @param author autore del messaggio
     * @param message messaggio da inviare
     * @param contentId id del contenuto
     * @param reciver destinatario del messaggio
     */
	public void notify(String author, String message, String contentId, String reciver) {
        Notification n1 = new Notification(author, message, contentId, reciver);
        notificationRepository.save(n1);
	}
	
	/**
	 * motodo per notificare ad un gran numero di persone
	 * qualcosa relativo ad un contenuto
	 * @param author autore del messaggio
	 * @param message messaggio da inviare
	 * @param contentId id del contenuto
	 * @param receivers destinatari della notifica
	 */
	public void notifyEvent(String author, String message, String contentId, List<String> receivers) {
        List<String> l1 = receivers;
        for (String s : l1) {
            notify(author, message, contentId, s);
        }
    }
	
	/**
	 * metodo per ottenere i messaggi di un dato utente
	 * @param username nome utente dell'utente
	 * @return messaggi dell'utente
	 */
	public List<Notification> getMyMessages(String username){
		return this.notificationRepository.findByReceiver(username);
	}
	
	/**
	 * metodo per ottere un messaggio e leggerlo
	 * @param username nome utente
	 * @param id id del messaggio
	 * @return messaggio desiderato
	 */
	public Notification getMyMessage(String username, String id) {
		Notification msg = notificationRepository.findById(id).orElse(null);
		if(msg != null) {
			msg.setRead(true);
            notificationRepository.save(msg);
		}
		return msg;
	}

	/**
	 * metodo per eliminare la casella di notifiche
	 * di un dato utente
	 * @param username nome utente da eliminare
	 */
	public void deleteUser(String username) {
		this.notificationRepository.deleteAll(getMyMessages(username));
	}
}
