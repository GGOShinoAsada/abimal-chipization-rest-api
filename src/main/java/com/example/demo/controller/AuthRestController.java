package com.example.demo.controller;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.dto.AccountViewDto;
import com.example.demo.service.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

/**
 * контроллер авторизации и регистрации
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Slf4j
@RestController
@Validated
public class AuthRestController {


    private final UserDetailsService userDetailsService;

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthRestController(UserDetailsService userDetailsService, AccountService accountService, AccountMapper accountMapper, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.authenticationManager = authenticationManager;
    }

    /**
     * метод-заглушка для обработки входа в приложение
     * может быть выполнен как авторизованными, так и неавторизованными пользователями
     * @param principal
     * @param request
     * @return авторизованный аккаунт AccountDto или null
     */
    @PostMapping("/login")
    public @ResponseBody AccountDto login(Principal principal, HttpServletRequest request)
    {
        if (principal!=null)
        {
            log.info("load user information");
            Account user = (Account) userDetailsService.loadUserByUsername(principal.getName());
            return accountMapper.toDto(user);
        }
        else {
            log.warn("user is not authorized");
            return null;
        }
    }

    /**
     * метод регистрации новых пользователей
     * может быть выполнен только неавторизованными пользователями
     * @param dto
     * @param principal
     * @param request
     * @return
     * 201 - запрос успешно выполнен;
     * 400 - неверные параметры запроса;
     * 403 - запрос от авторизованного аккаунта;
     * 409 - аккаунт с таким email уже существует;
     */
    @PostMapping("/registration")
    public ResponseEntity<AccountViewDto> register(@Valid @RequestBody AccountDto dto, Principal principal, HttpServletRequest request)
    {
        log.info("register new account");

        try
        {
            String password = dto.getPassword();
            Optional<AccountDto> box = accountService.add(dto, principal);
            if (box.isPresent())
            {
                AccountDto item = box.get();
                log.info("register success, created account id is {}", box.get().getId());
                log.info("login in the system");
                authenticateUserAndSetSession(dto.getEmail(), password, request);
                String authUserName = SecurityContextHolder.getContext().getAuthentication().getName();
                if (authUserName!=null)
                {
                    log.info("success authorize user with email "+authUserName);
                    return new ResponseEntity(accountMapper.convertDtoToViewDto(item), HttpStatus.CREATED);
                }
                else
                {
                    log.warn("authorization failed");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }


            }
            else {
                log.error("register failed");
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        }
        catch (ResponseStatusException ex)
        {
            log.warn(ex.getMessage());
            return new ResponseEntity<>(ex.getStatusCode());
        }
    }

    /**
     * метод-заглушка для обработки выхода из приложения
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request)
    {
        log.info("clear session");
        request.getSession().invalidate();
        log.info("logout success");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * выплнение авторизации после регистрации
     * @param username
     * @param password
     * @param request
     */
    private void authenticateUserAndSetSession(String username, String password, HttpServletRequest request)
    {
        log.info("#1. username {}, password {}",username, password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        request.getSession();
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        token.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }


}
