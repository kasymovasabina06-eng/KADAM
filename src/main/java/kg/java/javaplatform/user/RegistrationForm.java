package kg.java.javaplatform.user;
import jakarta.validation.constraints.*;
public class RegistrationForm {
 @NotBlank @Size(min=2,max=120) private String fullName;
 @NotBlank @Email @Size(max=190) private String email;
 @NotBlank @Size(min=8,max=72) private String password;
 @NotNull private Role role;
 @Size(max=100) private String city;
 @Size(max=160) private String specialization;
 public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getPassword(){return password;} public void setPassword(String v){password=v;}
 public Role getRole(){return role;} public void setRole(Role v){role=v;}
 public String getCity(){return city;} public void setCity(String v){city=v;}
 public String getSpecialization(){return specialization;} public void setSpecialization(String v){specialization=v;}
}
