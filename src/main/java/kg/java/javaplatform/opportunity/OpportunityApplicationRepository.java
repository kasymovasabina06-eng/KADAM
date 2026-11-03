package kg.java.javaplatform.opportunity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OpportunityApplicationRepository
        extends JpaRepository<OpportunityApplication, Long> {

    boolean existsByOpportunityIdAndApplicantEmail(
            Long opportunityId,
            String applicantEmail
    );
}
