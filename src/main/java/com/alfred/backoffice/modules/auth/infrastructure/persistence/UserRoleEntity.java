package com.alfred.backoffice.modules.auth.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_roles")
public class UserRoleEntity {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(optional = false)
    @MapsId("userUuid")
    @JoinColumn(name = "user", referencedColumnName = "uuid", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(optional = false)
    @MapsId("roleUuid")
    @JoinColumn(name = "role", referencedColumnName = "uuid", nullable = false)
    private RoleEntity roleEntity;

}
