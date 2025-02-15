package instagram.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isLike;
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    private Post post;
    @ToString.Exclude
    @ManyToOne
    private Comment comment;
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;
}
