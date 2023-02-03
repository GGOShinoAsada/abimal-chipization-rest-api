package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public @ResponseBody Account getAuthUser(Principal principal)
    {
        if (principal.getName()!=null)
        {
            log.info("load user information");
            Account user = (Account) userDetailsService.loadUserByUsername(principal.getName());
            return user;
        }
        else {
            log.warn("user is not authorized");
            return null;
        }
    }

    @PostMapping("/logout")
    public void logout()
    {
        log.info("logout success");
    }
}
