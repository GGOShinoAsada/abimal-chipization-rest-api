package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RootRestController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String onlyUsers()
    {
        return "only users can see this";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String onlyAdmins()
    {
        return "only admins can see this";
    }
}
