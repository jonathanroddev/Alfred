package com.alfred.backoffice.modules.auth.domain.repository;

import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserStatusRepository extends JpaRepository<UserStatusEntity, String> {
}
