package com.iamdemo.identity_access_governance.service;

import com.iamdemo.identity_access_governance.model.User;
import com.iamdemo.identity_access_governance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.iamdemo.identity_access_governance.model.Role;
import com.iamdemo.identity_access_governance.repository.RoleRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository; 
    private final AuditLogService auditLogService;
    private final PasswordEncoder encoder;
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       AuditLogService auditLogService, PasswordEncoder encoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.auditLogService = auditLogService;
    this.encoder = encoder;
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email already exists!");
        }
        try {
            // save once, then audit, then return — all INSIDE the method
            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                user.setPassword(encoder.encode(user.getPassword()));
            }
            User saved = userRepository.save(user);
            auditLogService.record("system", "USER_CREATE", "USER", saved.getId(), saved.getUsername());
            return saved;
            } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new DuplicateResourceException("Username or Email already exists");
            }
        
        
    }


    @Transactional
    public User assignRole(Long userId, Long roleId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    Role role = roleRepository.findById(roleId)
        .orElseThrow(() -> new RuntimeException("Role not found"));
    user.getRoles().add(role); // Set prevents duplicates
    if (user.getRoles().add(role)) {
            auditLogService.record("system", "ROLE_ASSIGN", "USER", user.getId(), "roleId=" + role.getId()); // add
        }
    return user;
    }
}
