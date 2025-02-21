package instagram.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
    @OneToMany(mappedBy = "post",cascade = {CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Comment> comments;
    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ToString.Exclude
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Image> images = new ArrayList<>();
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private List<Like> likes;

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_user_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> taggedUsers = new ArrayList<>();

    @PrePersist @PreUpdate
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
