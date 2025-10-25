package com.iamdemo.identity_access_governance.service;
import com.iamdemo.identity_access_governance.model.Role;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SodService {

    // Minimal conflict rules: edit/extend as needed
    private static final Map<String, Set<String>> CONFLICTS = Map.of(
       "ROLE_FINANCE", Set.of("ROLE_AUDIT", "ROLE_HR"),
        "ROLE_AUDIT",   Set.of("ROLE_FINANCE"),
        "ROLE_HR",      Set.of("ROLE_FINANCE")
    );

    // Use when you have Role entities
    public boolean hasConflict(Set<Role> currentRoles, Role requested) {
        String req = requested.getName();
        Set<String> blocked = CONFLICTS.getOrDefault(req, Collections.emptySet());
        for (Role r : currentRoles) {
            if (blocked.contains(r.getName())) return true;
        }
        return false;
    }

    // Optional helper if you only have names
    // public boolean hasConflictByNames(Set<String> currentRoleNames, String requestedName) {
    //     Set<String> blocked = CONFLICTS.getOrDefault(requestedName, Collections.emptySet());
    //     for (String r : currentRoleNames) {
    //         if (blocked.contains(r)) return true;
    //     }
    //     return false;
    // }
}
