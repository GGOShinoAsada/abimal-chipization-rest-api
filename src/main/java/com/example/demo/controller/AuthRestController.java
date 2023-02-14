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
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping("/login")
    public @ResponseBody AccountDto login(Principal principal)
    {
        if (principal.getName()!=null)
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
                log.info("login name is "+authUserName);
                if (authUserName!=null)
                {
                    log.info("success authorize user with email "+authUserName);
                }
                else
                {
                    log.warn("authorization failed");
                }
                return new ResponseEntity(accountMapper.convertDtoToViewDto(item), HttpStatus.CREATED);
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


    private void authenticateUserAndSetSession(String username, String password, HttpServletRequest request) {

        log.info("#1. username {}, password {}",username, password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        request.getSession();
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        token.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }



  /*  @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidatorExceptions(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors = new HashMap();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return errors;
    }*/

}
