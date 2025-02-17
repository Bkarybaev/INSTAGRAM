package instagram.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String commentText;
    private LocalDate createdAt;
    @ToString.Exclude
    @ManyToOne
    private Post post;
    @ToString.Exclude
    @OneToOne
    private User user;
    @ToString.Exclude
    @OneToMany(mappedBy = "comment",cascade = CascadeType.ALL)
    private List<Like> likes;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
