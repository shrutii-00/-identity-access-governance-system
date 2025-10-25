package com.iamdemo.identity_access_governance.controller;

import com.iamdemo.identity_access_governance.model.AccessRequest;
import com.iamdemo.identity_access_governance.service.AccessRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
public class AccessRequestController {
    private final AccessRequestService service;
    public AccessRequestController(AccessRequestService service) { this.service = service; }

    @GetMapping("/pending")
    public List<AccessRequest> listPending() {
        return service.listPending();
    }

    @PostMapping
    public ResponseEntity<AccessRequest> create(@RequestBody Map<String,Object> body) {
        Long userId = ((Number) body.get("userId")).longValue();
        Long roleId = ((Number) body.get("roleId")).longValue();
        String reason = (String) body.getOrDefault("reason", "");
        AccessRequest saved = service.create(userId, roleId, reason);
        return ResponseEntity.created(URI.create("/api/requests/" + saved.getId())).body(saved);
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<AccessRequest> resolve(@PathVariable Long id,
                                                 @RequestParam String action,
                                                 @RequestParam(defaultValue = "admin") String approver) {
        return ResponseEntity.ok(service.resolve(id, action, approver));
    }
}
