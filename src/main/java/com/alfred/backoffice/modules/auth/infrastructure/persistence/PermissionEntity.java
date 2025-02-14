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
@Table(name = "permissions",  uniqueConstraints = @UniqueConstraint(columnNames = {"resource", "operation"}))
public class PermissionEntity {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resource", referencedColumnName = "name", nullable = false)
    private ResourceEntity resource;

    @ManyToOne(optional = false)
    @JoinColumn(name = "operation", referencedColumnName = "name", nullable = false)
    private OperationEntity operationEntity;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }
}
