package com.alfred.backoffice.modules.auth.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_groups")
public class UserGroupEntity {

    @EmbeddedId
    private UserGroupId id;

    @ManyToOne(optional = false)
    @MapsId("userUuid")
    @JoinColumn(name = "user", referencedColumnName = "uuid", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(optional = false)
    @MapsId("userTypeName")
    @JoinColumn(name = "user_type", referencedColumnName = "name", nullable = false)
    private UserTypeEntity userTypeEntity;

}

