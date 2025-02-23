package instagram.repository.impl;

import instagram.models.Sms;
import instagram.models.User;
import instagram.repository.SmsRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SmsRepoImpl implements SmsRepo {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Sms> getMessages(User user, User chatUser) {
        return em.createQuery("""
                        SELECT s FROM Sms s
                        WHERE (s.sender.id = :userId AND s.receiver.id = :chatUserId)
                        OR (s.sender.id = :chatUserId AND s.receiver.id = :userId)
                        ORDER BY s.time ASC
                        """, Sms.class)
                .setParameter("userId", user.getId())
                .setParameter("chatUserId", chatUser.getId())
                .getResultList();
    }

    @Override
    @Transactional
    public void sendSms(User sender, User receiver, String message) {
        User senderUser = em.find(User.class, sender.getId());
        User receiverUser = em.find(User.class, receiver.getId());
        em.createNativeQuery("""
                        insert into sms (sms,time,receiver_id,sender_id,isread)
                        values (?,?,?,?,?)
                        """)
                .setParameter(1, message)
                .setParameter(2, LocalDateTime.now())
                .setParameter(3, receiverUser.getId())
                .setParameter(4, senderUser.getId())
                .setParameter(5, false)
                .executeUpdate();
    }

    @Override
    public int countUnreadMessages(User user) {
        return em.createQuery("SELECT COUNT(s) FROM Sms s WHERE s.receiver = :user AND s.isRead = false", Long.class)
                .setParameter("user", user)
                .getSingleResult()
                .intValue();

    }

    @Transactional
    public void markMessagesAsRead(User receiver, User sender) {
        em.createQuery("UPDATE Sms s SET s.isRead = true WHERE s.receiver = :receiver AND s.sender = :sender AND s.isRead = false")
                .setParameter("receiver", receiver)
                .setParameter("sender", sender)
                .executeUpdate();
    }

    @Override
    public Map<Long, Integer> countUnreadMessagesPerUser(User currentUser) {
        List<Object[]>  results = em.createQuery("""
                            SELECT s.sender.id, COUNT(s)
                            FROM Sms s
                            WHERE s.receiver.id = :currentUserId AND s.isRead = false
                            GROUP BY s.sender.id
                        """)
                .setParameter("currentUserId", currentUser.getId())
                .getResultList();

        Map<Long, Integer> unreadCounts = new HashMap<>();
        for (Object[] result : results) {
            unreadCounts.put((Long) result[0], ((Number) result[1]).intValue());
        }
        return unreadCounts;

    }

}
