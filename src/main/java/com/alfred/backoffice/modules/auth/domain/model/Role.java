package com.alfred.backoffice.modules.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {
    private String uuid;
    private String name;
    private Community community;
    private Set<PermissionRole> permissionRoles;
}
