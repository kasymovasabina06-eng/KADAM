package kg.java.javaplatform.user;
import jakarta.persistence.*;
@Entity @Table(name="app_users", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class AppUser {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=120) private String fullName;
 @Column(nullable=false,unique=true,length=190) private String email;
 @Column(nullable=false) private String passwordHash;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=40) private Role role;
 @Column(length=100) private String city;
 @Column(length=160) private String specialization;
 protected AppUser() {}
 public AppUser(String name,String email,String passwordHash,Role role,String city,String specialization) {this.fullName=name;this.email=email;this.passwordHash=passwordHash;this.role=role;this.city=city;this.specialization=specialization;}
 public Long getId(){return id;} public String getFullName(){return fullName;} public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;} public Role getRole(){return role;} public String getCity(){return city;} public String getSpecialization(){return specialization;}
}
