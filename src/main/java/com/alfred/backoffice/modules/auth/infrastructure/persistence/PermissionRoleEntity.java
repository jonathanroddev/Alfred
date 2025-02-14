package com.alfred.backoffice.modules.auth.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "permission_roles")
public class PermissionRoleEntity {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "permission", referencedColumnName = "uuid", nullable = false)
    private PermissionEntity permissionEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role", referencedColumnName = "uuid", nullable = false)
    private RoleEntity roleEntity;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }
}
