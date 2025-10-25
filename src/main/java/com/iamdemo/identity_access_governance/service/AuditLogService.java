package com.iamdemo.identity_access_governance.service;

import com.iamdemo.identity_access_governance.model.AuditLog;
import com.iamdemo.identity_access_governance.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {
    private final AuditLogRepository repo;
    public AuditLogService(AuditLogRepository repo) { this.repo = repo; }

    public void record(String actor, String action, String entityType, Long entityId, String details) {
        AuditLog log = new AuditLog();
        log.setActor(actor);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        repo.save(log);
    }
}
