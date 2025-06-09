package dev.timur.example.iotesp32s3.model;

import dev.timur.example.iotesp32s3.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;
    
    @Column(name="username", nullable = false, unique = true)
    private String username;
    
    @Column(name="email", nullable = false, unique = true)
    private String email;
    
    @Column(name="password", nullable = false)
    private String password;
    
    @Column(name="first_name")
    private String firstName;
    
    @Column(name="last_name")
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role = Role.USER_ROLE;
    
    @Column(name="active", nullable = false)
    private Boolean active = true;
    
    @Column(name="last_login")
    private LocalDateTime lastLogin;
} 