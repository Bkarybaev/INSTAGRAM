package instagram.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
public class Sms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sms;
    private LocalDateTime time;
    @PrePersist
    protected void onCreate() {
        this.time = LocalDateTime.now();
    }
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @Transient
    private String formattedTime; // <-- Бул жаңы талаа, базага сакталбайт



    @Column(nullable = false)
    private boolean isRead = false;

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
