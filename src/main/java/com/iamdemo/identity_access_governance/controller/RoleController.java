package com.iamdemo.identity_access_governance.controller;

import com.iamdemo.identity_access_governance.model.Role;
import com.iamdemo.identity_access_governance.repository.RoleRepository;
import com.iamdemo.identity_access_governance.service.AuditLogService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/roles")

public class RoleController {
    private final RoleRepository roleRepo;
    private final AuditLogService auditLogService;
    public RoleController(RoleRepository roleRepo, AuditLogService auditLogService) { 
        this.roleRepo = roleRepo; 
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Role req) {
        if (req.getName() == null || req.getName().isBlank()) return ResponseEntity.badRequest().build();
        if (roleRepo.existsByName(req.getName())) return ResponseEntity.status(409).build();
        Role saved = roleRepo.save(req);
        auditLogService.record("system", "ROLE_CREATE", "ROLE", saved.getId(), saved.getName()); // add
        return ResponseEntity.created(URI.create("/api/roles/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<Role> list() { return roleRepo.findAll(); }
}
