package com.alfred.backoffice.modules.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
public class RoleDTO {
    private String uuid;
    private String name;
    private CommunityDTO community;
    private Set<PermissionRoleDTO> permissionRoles;
}
