package com.alfred.backoffice.modules.auth.infrastructure.persistence;

import com.alfred.backoffice.modules.auth.domain.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ToString(exclude = {"roles", "userTypes"})
@EqualsAndHashCode(exclude = {"roles", "userTypes"})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"external_uuid", "community"})
        })
public class UserEntity {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "external_uuid", nullable = false, updatable = false)
    private String externalUuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_status", referencedColumnName = "name", nullable = false)
    private UserStatusEntity userStatus;

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

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "role")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "user_type")
    )
    private Set<UserTypeEntity> userTypes = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.createdBy = currentUser.getUuid();
        this.updatedBy = currentUser.getUuid();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}

