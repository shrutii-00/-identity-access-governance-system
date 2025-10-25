package com.iamdemo.identity_access_governance.controller;

import com.iamdemo.identity_access_governance.model.User;
import com.iamdemo.identity_access_governance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")

public class UserController {
    @Autowired

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User req) {
    User saved = userService.registerUser(req);
    return ResponseEntity.status(201).body(saved);
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<User> addRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        User updated = userService.assignRole(userId, roleId);
        return ResponseEntity.ok(updated);
    }

}