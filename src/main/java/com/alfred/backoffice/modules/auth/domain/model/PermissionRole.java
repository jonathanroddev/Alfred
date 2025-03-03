package com.alfred.backoffice.modules.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class PermissionRole {
    private String uuid;
    private Permission permission;
    private Role role;
}
