package instagram.models;

import instagram.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Repository;

@Entity
@Table(name = "users_info")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String biography;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String imageUrl;
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;
}
