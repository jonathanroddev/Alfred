package com.alfred.backoffice.modules.auth.application.request;

import com.alfred.backoffice.modules.auth.application.dto.response.CommunityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSignup {
    private String mail;
    private CommunityDTO communityDTO;
}
