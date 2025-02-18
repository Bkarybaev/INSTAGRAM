package instagram.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String commentText;
    private LocalDate createdAt;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ToString.Exclude
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
