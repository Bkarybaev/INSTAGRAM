package instagram.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isLike;
    @ToString.Exclude
    @ManyToOne
    private Post post;
    @ToString.Exclude
    @ManyToOne
    private Comment comment;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn
    private User user;
}
