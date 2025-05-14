package com.alfred.backoffice.modules.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
public class UserDTO {
    private String uuid;
    private String externalUuid;
    private UserStatusDTO userStatus;
    private CommunityDTO community;
    private Set<RoleDTO> roles;
    private Set<UserTypeDTO> userTypes;
}
