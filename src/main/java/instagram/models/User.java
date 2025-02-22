package instagram.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.processing.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(unique = true)
    private String email;
    private String phoneNumber;

    @ToString.Exclude
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private UserInfo userInfo;
    @ToString.Exclude
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Follower follower;
    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = {CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Comment> comment;
    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Post> posts;

    @ToString.Exclude
    @ManyToMany(mappedBy = "taggedUsers",fetch = FetchType.EAGER)
    private List<Post> taggedPosts = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Image> image;

    @ToString.Exclude
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER,
    orphanRemoval = true)
    private List<Like> likes;

    public User(String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
