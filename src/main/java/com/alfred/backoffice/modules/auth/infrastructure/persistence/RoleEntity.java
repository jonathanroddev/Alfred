package com.alfred.backoffice.modules.auth.infrastructure.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "name", length = 50, nullable = false, updatable = false, unique = true)
    @Size(max = 50, message = "Name must be at most 50 characters long.")
    @NotBlank(message = "Name cannot be blank.")
    @Pattern(regexp = "\\S.*\\S|\\S", message = "Name cannot contain only spaces.")
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "community", referencedColumnName = "uuid", nullable = false)
    private CommunityEntity community;

    @Column(name = "_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "_created_by", length = 255, nullable = false, updatable = false)
    @Size(max = 255, message = "Created by must be at most 255 characters long.")
    @NotBlank(message = "Created by cannot be blank.")
    @Pattern(regexp = "\\S.*\\S|\\S", message = "Created by cannot contain only spaces.")
    private String createdBy;

    @Column(name = "_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "_updated_by", length = 255, nullable = false)
    @Size(max = 255, message = "Updated by must be at most 255 characters long.")
    @NotBlank(message = "Updated by cannot be blank.")
    @Pattern(regexp = "\\S.*\\S|\\S", message = "Updated by cannot contain only spaces.")
    private String updatedBy;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<PermissionRoleEntity> permissionsRoles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}

