package instagram.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(cascade = CascadeType.ALL)
    private Post post;

    @ManyToOne
    private Comment comment;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;
}
