package com.iamdemo.identity_access_governance.service;

import com.iamdemo.identity_access_governance.model.*;
import com.iamdemo.identity_access_governance.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iamdemo.identity_access_governance.service.AuditLogService;

import java.time.Instant;
import java.util.List;

@Service
public class AccessRequestService {
    private final AccessRequestRepository reqRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserService userService;
    private final AuditLogService audit; // for assignRole
    private final SodService sod;


    public AccessRequestService(AccessRequestRepository reqRepo, UserRepository userRepo,
                                RoleRepository roleRepo, UserService userService, AuditLogService audit, SodService sod) {
        this.reqRepo = reqRepo; this.userRepo = userRepo;
        this.roleRepo = roleRepo; this.userService = userService;
        this.audit = audit;
        this.sod = sod;
    }

    public List<AccessRequest> listPending() {
        return reqRepo.findByStatus(AccessRequest.Status.PENDING);
    }

    @Transactional
    public AccessRequest create(Long userId, Long roleId, String reason) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        AccessRequest ar = new AccessRequest();
        ar.setUser(user); ar.setRole(role); ar.setReason(reason);
        ar.setStatus(AccessRequest.Status.PENDING);
        // return reqRepo.save(ar);
        AccessRequest saved = reqRepo.save(ar);
        audit.record("system", "REQ_CREATE", "REQUEST", saved.getId(),
                     "userId=" + user.getId() + ", roleId=" + role.getId()); // add
        return saved;
    }

    // @Transactional
    // public AccessRequest resolve(Long reqId, String action, String approver) {
    //     AccessRequest ar = reqRepo.findById(reqId).orElseThrow(() -> new RuntimeException("Request not found"));
    //     if (!ar.getStatus().equals(AccessRequest.Status.PENDING)) return ar;

    //     if ("APPROVED".equalsIgnoreCase(action)) {
    //         userService.assignRole(ar.getUser().getId(), ar.getRole().getId());
    //         ar.setStatus(AccessRequest.Status.APPROVED);
    //         audit.record(approver, "REQ_APPROVE", "REQUEST", ar.getId(),
    //                      "userId=" + ar.getUser().getId() + ", roleId=" + ar.getRole().getId()); // add
    //         // 1) Duplicate guard
    //     boolean alreadyHas = ar.getUser().getRoles().stream()
    //             .anyMatch(r -> r.getName().equals(ar.getRole().getName()));
    //     if (alreadyHas) {
    //         ar.setStatus(AccessRequest.Status.REJECTED);
    //         String reason = ar.getReason() == null ? "" : ar.getReason() + " | ";
    //         ar.setReason(reason + "Already has role");
    //         audit.record(approver, "REQ_REJECT", "REQUEST", ar.getId(), "Already has role");
    //     } else {
    //         // 2) SoD check
    //         boolean conflict = sod.hasConflict(ar.getUser().getRoles(), ar.getRole());
    //         if (conflict) {
    //             ar.setStatus(AccessRequest.Status.REJECTED);
    //             String reason = ar.getReason() == null ? "" : ar.getReason() + " | ";
    //             ar.setReason(reason + "SoD conflict with existing roles");
    //             audit.record(approver, "REQ_REJECT", "REQUEST", ar.getId(), "SoD conflict");
    //         } else {
    //             // 3) OK to approve
    //             userService.assignRole(ar.getUser().getId(), ar.getRole().getId());
    //             ar.setStatus(AccessRequest.Status.APPROVED);
    //             audit.record(approver, "REQ_APPROVE", "REQUEST", ar.getId(),
    //                          "userId=" + ar.getUser().getId() + ", roleId=" + ar.getRole().getId());
    //         }
    //     }
    //     } else {
    //         ar.setStatus(AccessRequest.Status.REJECTED);
    //         audit.record(approver, "REQ_REJECT", "REQUEST", ar.getId(),
    //                      "reason=" + (ar.getReason() == null ? "" : ar.getReason())); // add
    //     }
    //     ar.setDecidedBy(approver);
    //     ar.setDecidedAt(java.time.Instant.now());
    //     return ar;
    // }

    @Transactional
    public AccessRequest resolve(Long reqId, String action, String approver) {
        AccessRequest ar = reqRepo.findById(reqId)
        .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!ar.getStatus().equals(AccessRequest.Status.PENDING)) {
            return ar; // already resolved
        }

        if ("APPROVED".equalsIgnoreCase(action)) {
        // 1) Check if user already has this role
            boolean alreadyHas = ar.getUser().getRoles().stream()
            .anyMatch(r -> r.getName().equals(ar.getRole().getName()));

            if (alreadyHas) {
            ar.setStatus(AccessRequest.Status.REJECTED);
            String reason = (ar.getReason() == null ? "" : ar.getReason() + " | ");
            ar.setReason(reason + "Already has role");
            audit.record(approver, "REQ_REJECT", "REQUEST", ar.getId(), "Already has role");
            } else {
            // 2) Check SoD conflict
            boolean conflict = sod.hasConflict(ar.getUser().getRoles(), ar.getRole());
            if (conflict) {
                ar.setStatus(AccessRequest.Status.REJECTED);
                String reason = (ar.getReason() == null ? "" : ar.getReason() + " | ");
                ar.setReason(reason + "SoD conflict with existing roles");
                audit.record(approver, "REQ_REJECT", "REQUEST", ar.getId(), "SoD conflict");
            } else {
                // 3) All clear → approve and assign
                    userService.assignRole(ar.getUser().getId(), ar.getRole().getId());
                    ar.setStatus(AccessRequest.Status.APPROVED);
                    audit.record(approver, "REQ_APPROVE", "REQUEST", ar.getId(),
                    "userId=" + ar.getUser().getId() + ", roleId=" + ar.getRole().getId());
                }
            }
        } else {
        // Manual reject
        ar.setStatus(AccessRequest.Status.REJECTED);
        audit.record(approver, "REQ_REJECT", "REQUEST", ar.getId(), "manual reject");
    }

        ar.setDecidedBy(approver);
        ar.setDecidedAt(java.time.Instant.now());
        return ar;
    }


}
