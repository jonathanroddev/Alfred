package com.alfred.backoffice.modules.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Community {

    private String uuid;
    private String name;
    private Plan plan;
}
