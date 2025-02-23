package instagram.repository;

import instagram.models.Sms;
import instagram.models.User;

import java.util.List;
import java.util.Map;

public interface SmsRepo {
    List<Sms> getMessages(User user, User chatUser);

    void sendSms(User sender, User receiver, String message);

    int countUnreadMessages(User user);

    void markMessagesAsRead(User user, User chatUser);

    Map<Long, Integer> countUnreadMessagesPerUser(User user);
}
