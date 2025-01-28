package com.alfred.backoffice.modules.example.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api.v1.path}/${module.example.path}")
public class GetExampleController {

    @GetMapping(path = "/salute")
    String getSalute() {
        return "Hello World, I'm Alfred!";
    }
}
