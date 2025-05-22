package com.alfred.backoffice.modules.auth.application.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Data
public class UserTypeDTO {
    private String name;
    private int level;
}
