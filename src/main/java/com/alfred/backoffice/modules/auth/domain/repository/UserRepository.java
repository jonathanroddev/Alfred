package com.alfred.backoffice.modules.auth.domain.repository;

import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByExternalUuidAndCommunityUuid(String externalUuid, UUID communityId);
    List<UserEntity> findAllByExternalUuid(String externalUuid);
    Optional<UserEntity> findByUuidAndExternalUuid(UUID uuid, String externalUuid);
}
