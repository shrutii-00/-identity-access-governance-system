package com.iamdemo.identity_access_governance.model;

import jakarta.persistence.*;
//import java.util.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    private boolean active = true;

    // @ElementCollection(fetch = FetchType.EAGER)
    // private Set<String> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Set<Role>  roles = new HashSet<>();

    // Constructors
    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // public Set<String> getRoles() { return roles; }
    // public void setRoles(Set<String> roles) { this.roles = roles; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
