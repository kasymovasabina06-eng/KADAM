package kg.java.javaplatform.config;
import kg.java.javaplatform.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean UserDetailsService userDetailsService(UserRepository repo){return email -> repo.findByEmail(email).map(u -> User.withUsername(u.getEmail()).password(u.getPasswordHash()).roles(u.getRole().name()).build()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));}
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  http.authorizeHttpRequests(a -> a.requestMatchers("/","/register","/login","/css/**","/js/**","/favicon.ico","/error").permitAll()
   .requestMatchers("/dashboard/organization").hasRole("ORGANIZATION")
   .requestMatchers("/dashboard/volunteer").hasRole("VOLUNTEER")
   .requestMatchers("/dashboard/student").hasAnyRole("INTERNSHIP_STUDENT","PRACTICE_STUDENT")
   .requestMatchers("/dashboard/help").hasRole("HELP_SEEKER")
   .anyRequest().authenticated());
  http.formLogin(f -> f.loginPage("/login").usernameParameter("email").defaultSuccessUrl("/dashboard",true).permitAll());
  http.logout(l -> l.logoutSuccessUrl("/?logout").permitAll());
  return http.build();
 }
}
