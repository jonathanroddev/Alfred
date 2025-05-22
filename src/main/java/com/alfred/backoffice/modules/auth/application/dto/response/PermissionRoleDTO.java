package com.alfred.backoffice.modules.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PermissionRoleDTO {
    private String uuid;
    private PermissionDTO permission;
    private RoleDTO role;
}
