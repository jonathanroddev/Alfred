package com.alfred.backoffice.modules.auth.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupId implements Serializable {

    @Column(name = "user", nullable = false)
    private UUID userUuid;

    @Column(name = "user_type", nullable = false)
    private String userTypeName;
}
