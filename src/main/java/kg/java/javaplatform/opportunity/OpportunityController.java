package kg.java.javaplatform.opportunity;

import kg.java.javaplatform.user.AppUser;
import kg.java.javaplatform.user.Role;
import kg.java.javaplatform.user.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/opportunities")
public class OpportunityController {

    private final OpportunityRepository opportunityRepository;
    private final OpportunityApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public OpportunityController(
            OpportunityRepository opportunityRepository,
            OpportunityApplicationRepository applicationRepository,
            UserRepository userRepository
    ) {
        this.opportunityRepository = opportunityRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String opportunities(
            @RequestParam(required = false) String category,
            Model model
    ) {

        if (category == null || category.isBlank()) {
            model.addAttribute(
                    "opportunities",
                    opportunityRepository.findAllByOrderByCreatedAtDesc()
            );
        } else {
            model.addAttribute(
                    "opportunities",
                    opportunityRepository.findByCategoryOrderByCreatedAtDesc(
                            category
                    )
            );
        }

        return "opportunities";
    }

    @GetMapping("/create")
    public String createPage(Authentication authentication) {

        if (!isOrganization(authentication)) {
            return "redirect:/opportunities";
        }

        return "create-opportunity";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam String city,
            @RequestParam String description,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        if (!isOrganization(authentication)) {
            return "redirect:/opportunities";
        }

        title = title.trim();
        city = city.trim();
        description = description.trim();

        if (title.isEmpty()
                || title.length() > 150
                || city.isEmpty()
                || city.length() > 100
                || description.isEmpty()
                || description.length() > 3000
                || !isValidCategory(category)) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Проверьте правильность заполнения полей."
            );

            return "redirect:/opportunities/create";
        }

        Opportunity opportunity = new Opportunity(
                authentication.getName(),
                title,
                category,
                city,
                description
        );

        opportunityRepository.save(opportunity);

        redirectAttributes.addFlashAttribute(
                "success",
                "Объявление успешно опубликовано!"
        );

        return "redirect:/opportunities";
    }

    @PostMapping("/{id}/apply")
    public String apply(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {

            return "redirect:/login";
        }

        AppUser applicant = userRepository
                .findByEmail(authentication.getName())
                .orElse(null);

        if (applicant == null || applicant.getRole() == Role.ORGANIZATION) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Откликаться могут только пользователи, не являющиеся организациями."
            );
            return "redirect:/opportunities";
        }

        Opportunity opportunity = opportunityRepository
                .findById(id)
                .orElse(null);

        if (opportunity == null) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Объявление не найдено."
            );
            return "redirect:/opportunities";
        }

        boolean alreadyApplied =
                applicationRepository.existsByOpportunityIdAndApplicantEmail(
                        id,
                        authentication.getName()
                );

        if (alreadyApplied) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Вы уже откликнулись на это объявление."
            );
            return "redirect:/opportunities";
        }

        OpportunityApplication application =
                new OpportunityApplication(
                        opportunity,
                        authentication.getName()
                );

        applicationRepository.save(application);

        redirectAttributes.addFlashAttribute(
                "success",
                "Ваш отклик успешно отправлен!"
        );

        return "redirect:/opportunities";
    }

    private boolean isOrganization(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return userRepository
                .findByEmail(authentication.getName())
                .map(user -> user.getRole() == Role.ORGANIZATION)
                .orElse(false);
    }

    private boolean isValidCategory(String category) {

        return "INTERNSHIP".equals(category)
                || "PRACTICE".equals(category)
                || "VOLUNTEERING".equals(category)
                || "SOCIAL_HELP".equals(category);
    }
}
