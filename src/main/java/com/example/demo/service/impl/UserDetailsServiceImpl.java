package com.example.demo.service.impl;

import com.example.demo.model.Account;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private AccountRepository repository;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("load user by username");
        Account entity = null;
        if (username!=null || !username.equals(""))
        {

            Optional<Account> box = repository.findByEmail(username);
            if (box.isPresent())
            {
                entity = box.get();
                Set<Role> roles = new HashSet();
                roles.add(Role.ROLE_USER);
                entity.setRoles(roles);
            }
            else
            {
                log.warn("get more or less user items than 1");
            }
        }
        else {
            log.warn("username parameter is empty");
            entity = null;
        }
        return entity;
    }
}
