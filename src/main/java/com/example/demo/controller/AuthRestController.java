package com.example.demo.controller;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @Autowired
    public AuthRestController(UserDetailsService userDetailsService, AccountService accountService, AccountMapper accountMapper) {
        this.userDetailsService = userDetailsService;
        this.accountService = accountService;
        this.accountMapper = accountMapper;
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

    @PostMapping("/register")
    public ResponseEntity<AccountDto> register(@RequestBody AccountDto dto, Principal principal)
    {
        log.info("register new account");
        try
        {
            Optional<AccountDto> box = accountService.add(dto, principal);
            if (box.isPresent())
            {
                dto = box.get();
                log.info("register success, created account id is {}", dto.getId());
                return new ResponseEntity<>(dto, HttpStatus.CREATED);
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
