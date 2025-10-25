package com.iamdemo.identity_access_governance.repository;

import com.iamdemo.identity_access_governance.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // Optional handy finders
    List<AuditLog> findByActor(String actor);
    List<AuditLog> findByAction(String action);
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
    List<AuditLog> findByTimestampBetween(Instant from, Instant to);
}
