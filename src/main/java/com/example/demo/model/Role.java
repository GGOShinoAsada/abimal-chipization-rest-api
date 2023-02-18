package com.example.demo.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * перечисление Role
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
public enum Role implements GrantedAuthority {
    ROLE_USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
