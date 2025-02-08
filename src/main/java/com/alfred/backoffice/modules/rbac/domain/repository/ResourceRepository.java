package com.alfred.backoffice.modules.rbac.domain.repository;

import com.alfred.backoffice.modules.rbac.infrastructure.persistence.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<ResourceEntity, String> {
}
