package com.iamdemo.identity_access_governance.controller;

import com.iamdemo.identity_access_governance.model.AuditLog;
import com.iamdemo.identity_access_governance.repository.AuditLogRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auditlogs")
public class AuditLogController {
    private final AuditLogRepository repo;
    public AuditLogController(AuditLogRepository repo) { this.repo = repo; }

    @GetMapping
    public List<AuditLog> list() { return repo.findAll(); }
}
