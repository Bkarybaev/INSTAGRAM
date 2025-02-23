package instagram.service.impl;

import instagram.models.Sms;
import instagram.models.User;
import instagram.repository.SmsRepo;
import instagram.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SmsSerImpl implements SmsService {
    private final SmsRepo smsRepo;

    @Override
    public List<Sms> getMessages(User user, User chatUser) {
        return smsRepo.getMessages(user,chatUser);
    }

    @Override
    public void sendSms(User sender, User receiver, String message) {
        smsRepo.sendSms(sender,receiver,message);
    }

    @Override
    public int countUnreadMessages(User user) {
        return smsRepo.countUnreadMessages(user);
    }

    @Override
    public void markMessagesAsRead(User user, User chatUser) {
        smsRepo.markMessagesAsRead(user,chatUser);
    }

    @Override
    public Map<Long, Integer> countUnreadMessagesPerUser(User user) {
        return smsRepo.countUnreadMessagesPerUser(user);
    }
}
