package kg.java.javaplatform.opportunity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "kadam_opportunity_applications",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"opportunity_id", "applicant_email"}
        )
)
public class OpportunityApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunity opportunity;

    @Column(name = "applicant_email", nullable = false, length = 150)
    private String applicantEmail;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected OpportunityApplication() {
    }

    public OpportunityApplication(
            Opportunity opportunity,
            String applicantEmail
    ) {
        this.opportunity = opportunity;
        this.applicantEmail = applicantEmail;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
