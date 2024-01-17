package Synk.Api.Model.User.Notification;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, String> {
    List<Notification> findByReceiver(String receiver);
}
