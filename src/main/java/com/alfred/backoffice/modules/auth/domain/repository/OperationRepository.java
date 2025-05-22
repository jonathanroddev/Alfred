package com.alfred.backoffice.modules.auth.domain.repository;

import com.alfred.backoffice.modules.auth.infrastructure.persistence.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<OperationEntity, String> {
}
