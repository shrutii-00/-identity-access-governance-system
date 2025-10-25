package com.iamdemo.identity_access_governance.repository;

import com.iamdemo.identity_access_governance.model.AccessRequest;
import com.iamdemo.identity_access_governance.model.AccessRequest.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {
    List<AccessRequest> findByStatus(Status status);
}
