package com.alfred.backoffice.modules.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PermissionDTO {
    private String uuid;
    private ResourceDTO resource;
    private OperationDTO operation;
}
