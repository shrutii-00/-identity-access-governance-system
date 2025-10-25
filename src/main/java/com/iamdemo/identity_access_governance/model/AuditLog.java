package com.iamdemo.identity_access_governance.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Instant timestamp = Instant.now();
  @Column(length = 64)  private String actor;
  @Column(length = 64)  private String action;     // USER_CREATE, ROLE_CREATE, REQ_CREATE, REQ_APPROVE, REQ_REJECT, ROLE_ASSIGN
  @Column(length = 32)  private String entityType; // USER, ROLE, REQUEST
  private Long entityId;
  @Column(length = 512) private String details;

  // getters/setters
  public Long getId() { return id; }
  public Instant getTimestamp() { return timestamp; }
  public String getActor() { return actor; }
  public void setActor(String actor) { this.actor = actor; }
  public String getAction() { return action; }
  public void setAction(String action) { this.action = action; }
  public String getEntityType() { return entityType; }
  public void setEntityType(String entityType) { this.entityType = entityType; }
  public Long getEntityId() { return entityId; }
  public void setEntityId(Long entityId) { this.entityId = entityId; }
  public String getDetails() { return details; }
  public void setDetails(String details) { this.details = details; }
}
