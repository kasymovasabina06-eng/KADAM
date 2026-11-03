package kg.java.javaplatform.opportunity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kadam_opportunities")
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String organizationEmail;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 30)
    private String category;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 3000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Opportunity() {
    }

    public Opportunity(
            String organizationEmail,
            String title,
            String category,
            String city,
            String description
    ) {
        this.organizationEmail = organizationEmail;
        this.title = title;
        this.category = category;
        this.city = city;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getOrganizationEmail() {
        return organizationEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
