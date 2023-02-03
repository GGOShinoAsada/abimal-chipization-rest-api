package com.example.demo.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "account")
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "email", length = 50, nullable = false)
    @Email
    private String email;

    @Column(name = "password", length = 50, nullable = false)
    private String password;

    //@CollectionTable(name = "user_roles", joinColumns = {@JoinColumn(name = "account_id")})
    //@Enumerated(EnumType.STRING)
    //@ElementCollection(fetch = FetchType.EAGER)
    @Transient
    private Set<Role> roles = new HashSet();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
