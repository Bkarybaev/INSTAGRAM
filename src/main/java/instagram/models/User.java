package instagram.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(unique = true)
    private String username;
    private String password;

//    @Column(unique = true)
    private String email;
    private String phoneNumber;

    @ToString.Exclude
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private UserInfo userInfo;
    @ToString.Exclude
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Follower follower;
    @ToString.Exclude
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Comment comment;
    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Post> posts;
    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Image> image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Like likes;

    public User(String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


}
