package com.alfred.backoffice.modules.auth.domain.repository;

import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    // TODO: Remove this because it has to return a list instead of a single record
    Optional<UserEntity> findByExternalUuid(String externalUuid);
    List<UserEntity> findAllByExternalUuid(String externalUuid);
}
