package instagram.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@ToString
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ImageUrl;
    @ToString.Exclude
     @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    @ToString.Exclude
     @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;

}
