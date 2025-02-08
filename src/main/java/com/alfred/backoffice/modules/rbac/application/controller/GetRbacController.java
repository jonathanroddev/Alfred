package com.alfred.backoffice.modules.rbac.application.controller;

import com.alfred.backoffice.modules.rbac.application.dto.response.ResourceDTO;
import com.alfred.backoffice.modules.rbac.domain.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.v1.path}/${module.rbac.path}")
public class GetRbacController {

    private final ResourceService resourceService;

    @GetMapping(path = "/resources")
    List<ResourceDTO> getResources() {
        return resourceService.getAllResources();
    }
}
