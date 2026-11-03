
package kg.java.javaplatform.post;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/feed")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public String feed(Model model, Authentication authentication) {

        model.addAttribute(
                "posts",
                postRepository.findAllByOrderByCreatedAtDesc()
        );

        model.addAttribute(
                "currentUser",
                authentication.getName()
        );

        return "feed";
    }

    @PostMapping("/create")
    public String createPost(
            @RequestParam("content") String content,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        String text = content.trim();

        if (text.isEmpty() || text.length() > 2000) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Публикация должна содержать от 1 до 2000 символов."
            );
            return "redirect:/feed";
        }

        Post post = new Post(
                authentication.getName(),
                text
        );

        postRepository.save(post);

        redirectAttributes.addFlashAttribute(
                "success",
                "Публикация успешно создана!"
        );

        return "redirect:/feed";
    }
}
