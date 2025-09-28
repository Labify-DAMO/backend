package com.labify.backend.lab.request.repository;

import com.labify.backend.lab.request.entity.LabRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabRequestRepository extends JpaRepository<LabRequest, Long> {
}
