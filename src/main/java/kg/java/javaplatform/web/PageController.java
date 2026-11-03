package kg.java.javaplatform.web;
import kg.java.javaplatform.user.*;
import jakarta.validation.Valid;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller public class PageController {
 private final UserRepository repo; private final PasswordEncoder encoder;
 public PageController(UserRepository repo,PasswordEncoder encoder){this.repo=repo;this.encoder=encoder;}
 @GetMapping("/") public String home(){return "index";}
 @GetMapping("/login") public String login(){return "login";}
 @GetMapping("/register") public String register(Model model){if(!model.containsAttribute("form"))model.addAttribute("form",new RegistrationForm());model.addAttribute("roles",Role.values());return "register";}
 @PostMapping("/register") public String create(@Valid @ModelAttribute("form") RegistrationForm form,BindingResult errors,Model model,RedirectAttributes redirect){
  model.addAttribute("roles",Role.values());
  String email=form.getEmail()==null?"":form.getEmail().trim().toLowerCase(Locale.ROOT);
  if(repo.existsByEmail(email))errors.rejectValue("email","exists","Этот email уже зарегистрирован");
  if(errors.hasErrors())return "register";
  repo.save(new AppUser(form.getFullName().trim(),email,encoder.encode(form.getPassword()),form.getRole(),form.getCity(),form.getSpecialization()));
  redirect.addFlashAttribute("registered",true);return "redirect:/login";
 }
 @GetMapping("/dashboard") public String route(Authentication auth){
  if(auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ORGANIZATION")))return "redirect:/dashboard/organization";
  if(auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_VOLUNTEER")))return "redirect:/dashboard/volunteer";
  if(auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_HELP_SEEKER")))return "redirect:/dashboard/help";
  return "redirect:/dashboard/student";
 }
 @GetMapping("/dashboard/{type}") public String dashboard(@PathVariable String type,Authentication authentication,Model model){
  AppUser user=repo.findByEmail(authentication.getName()).orElseThrow();
  model.addAttribute("user",user);
  model.addAttribute("heading",switch(type){case "organization"->"Кабинет организации";case "volunteer"->"Мои волонтёрские проекты";case "student"->"Возможности для студентов";case "help"->"Мои обращения о помощи";default->"Кабинет";});
  return "dashboard";
 }
}
