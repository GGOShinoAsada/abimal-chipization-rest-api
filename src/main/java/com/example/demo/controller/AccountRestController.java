package com.example.demo.controller;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.AccountService;
import com.example.demo.service.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/accounts")
@Validated
public class AccountRestController {


    private final AccountService accountService;

    @Autowired
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts()
    {
        log.info("get all accounts");
        List<AccountDto> list = accountService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Integer id)
    {
        log.info("searching account by id {}", id);
        if (checkId(id))
        {
            Optional<AccountDto> box = accountService.findById(id);
            if (box.isPresent())
            {
                log.info("account find successfully");
                return new ResponseEntity<>(box.get(), HttpStatus.OK);
            }
            else
            {
                log.warn("user was not found");
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            log.warn("id is mandatory and must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search")
    public ResponseEntity<List<AccountDto>> searchAccount(AccountDto dto, Pageable pageable)
    {
        log.info("search account by parameters");
        List<AccountDto> dtoList = new ArrayList();
        Boolean isValidPagination = pageable.getPageNumber()>0 && pageable.getPageSize()>0;
        if (isValidPagination)
        {
            dtoList = accountService.search(dto, PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize()));
            return new ResponseEntity(dtoList, HttpStatus.OK);
        }
        else {
            log.warn("pagination parameters must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Integer id, @RequestBody AccountDto dto, Principal principal)
    {
        log.info("updating information about account");
        Boolean isValid = checkId(id);
        if (isValid)
        {
            dto.setId(id);
            try {
                Optional<AccountDto> box = accountService.update(dto, principal.getName());
                if (box.isPresent())
                {
                    log.info("updating success");
                    return new ResponseEntity(box.get(),HttpStatus.OK);
                }
                else
                {
                    log.warn("updating failed");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity(ex.getStatusCode());
            }
        }
        else
        {
            log.warn("id parameter is mandatory and must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer id, Principal principal)
    {
        log.info("removing account with id {}",id);
        if (checkId(id))
        {
            try
            {
                accountService.remove(id, principal.getName());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity<>(ex.getStatusCode());
            }

        }
        else {
            log.warn("id is mandatory and must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    private Boolean checkId(Integer id)
    {
        Boolean isAllowId =false;
        if (id!=null && !id.equals(""))
        {
            isAllowId = id>0;
        }
        return isAllowId;
    }


    /*
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
