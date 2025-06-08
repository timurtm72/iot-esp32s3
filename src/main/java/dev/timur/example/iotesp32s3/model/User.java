package dev.timur.example.iotesp32s3.model;

import dev.timur.example.iotesp32s3.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode()
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {
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

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;
    @Column(name="removed_at")
    private LocalDateTime removedAt;
    @Column(name="modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name="last_login")
    private LocalDateTime lastLogin;
    
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
    //
    @PreRemove
    public void toRemove() {
        this.removedAt = LocalDateTime.now();
    }
} 