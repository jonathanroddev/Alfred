package com.alfred.backoffice.modules.auth.application.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
@Data
public class CommunityDTO {
    @Nullable
    private String uuid;
    private String name;
    private PlanDTO plan;
}
