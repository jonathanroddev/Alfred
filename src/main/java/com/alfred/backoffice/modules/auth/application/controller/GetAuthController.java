package com.alfred.backoffice.modules.auth.application.controller;

import com.alfred.backoffice.modules.auth.application.dto.response.ResourceDTO;
import com.alfred.backoffice.modules.auth.domain.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RequiredArgsConstructor
@RestController
@RequestMapping("${api.v1.path}/${module.auth.path}")
public class GetAuthController {

    private final ResourceService resourceService;

    @GetMapping(path = "/resources")
    List<ResourceDTO> getResources() {
        return resourceService.getAllResources();
    }
}
