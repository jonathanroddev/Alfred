package com.alfred.backoffice.modules.auth.domain.repository;

import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserTypeRepository extends JpaRepository<UserTypeEntity, String> {
    List<UserTypeEntity> findByLevelGreaterThanEqual(int level);
}
