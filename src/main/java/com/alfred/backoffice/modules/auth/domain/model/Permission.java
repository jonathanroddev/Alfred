package com.alfred.backoffice.modules.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Permission {
    private String uuid;
    private Resource resource;
    private Operation operation;
}
