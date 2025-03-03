package com.alfred.backoffice.modules.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private String uuid;
    private String externalUuid;
    private UserStatus userStatus;
    private Community community;
    private Set<Role> roles;
    private Set<UserType> userTypes;
}
