package com.iamdemo.identity_access_governance.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = "name"))

public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String name; // e.g., ROLE_USER, ROLE_ADMIN

    @Column(length = 255)
    private String description;

    // getters/setters/equals/hashCode
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }
    @Override public int hashCode() { return Objects.hash(name); }
}
