package com.alfred.backoffice.modules.auth.domain.repository;

import com.alfred.backoffice.modules.auth.infrastructure.persistence.CommunityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommunityRepository extends JpaRepository<CommunityEntity, UUID> {
    Optional<CommunityEntity> findByName(String name);
}
