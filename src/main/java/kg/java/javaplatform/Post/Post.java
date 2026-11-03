
package kg.java.javaplatform.post;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kadam_posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String authorEmail;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Post() {
    }

    public Post(String authorEmail, String content) {
        this.authorEmail = authorEmail;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
