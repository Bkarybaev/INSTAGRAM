package instagram.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDate createdAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> comments;
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;
    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> images;
    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
