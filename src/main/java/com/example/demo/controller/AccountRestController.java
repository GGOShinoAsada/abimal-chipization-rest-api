package com.example.demo.controller;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.AccountService;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.dto.AccountViewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

/**
 * контроллер аккаунтов
 * @author ROMAN
 * @date 2023-02-16
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountRestController {


    private final AccountService accountService;

    @Autowired
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * получение всех аккаунтов
     * запрос может выполнять только пользователь с ролью user
     * @return
     * 200 - успешный поиск;
     * 401 - запрос от неваторизованного аккаунта, неверные авторизационнные данные;
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<AccountViewDto>> getAllAccounts()
    {
        log.info("get all accounts");
        List<AccountViewDto> list = accountService.findAll();
        if (list==null)
        {
            return new ResponseEntity(list, HttpStatus.OK);
        }
        else {
            log.warn("accounts was not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * получение информации об аккаунте пользователя
     * выполнять может только пользователь с ролью user
     * @param id
     * @return
     * 200 - запрос успешно выполнен;
     * 400 - неверные пареметры запроса;
     * 401 - запрос от неваторизованного аккаунта, неверные авторизационнные данные;
     * 400 - аккаунит с id не найден;
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<AccountViewDto> getAccountById(@PathVariable("id") Integer id, Principal principal)
    {
        log.info("searching account by id {}", id);
        if (checkId(id))
        {
            Optional<AccountViewDto> box = accountService.findById(principal.getName(),id);
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

    /**
     * поиск аккаунтов по параметрам
     * может выполнять только пользователь с ролью user
     * @param dto
     * @param pageable
     * @param principal
     * @return
     * 200 - запрос успешно выполнен;
     * 401 - запрос от неваторизованного аккаунта, неверные авторизационнные данные;
     * 400 - неверные параметры запроса;
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search")
    public ResponseEntity<List<AccountViewDto>> searchAccount(AccountDto dto, Pageable pageable, Principal principal)
    {
        log.info("search account by parameters");
        List<AccountViewDto> dtoList = new ArrayList();

        Boolean isValidPagination = pageable.getPageNumber()>=0 && pageable.getPageSize()>0;
        if (isValidPagination)
        {
            dtoList = accountService.search(principal.getName(),dto, pageable);
            return new ResponseEntity(dtoList, HttpStatus.OK);
        }
        else {
            log.warn("pagination parameters must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * обновление данных об аккаунте пользователя
     * запрос может выполнять только пользователь с ролью user
     * @param id
     * @param dto
     * @param principal
     * @return
     * 200 - запрос успешно выполнен;
     * 409 - аккаунт с таким email уже существует;
     * 403 - обновление не своего аккаунта, аккаунт не найден;
     * 401 - запрос от неваторизованного аккаунта, неверные авторизационнные данные;
     * 400 - неверные параметры запроса
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<AccountViewDto> updateAccount(@PathVariable Integer id, @Valid @RequestBody AccountDto dto, Principal principal)
    {
        log.info("updating information about account");
        Boolean isValid = checkId(id);
        if (isValid)
        {
            dto.setId(id);
            try {
                Optional<AccountViewDto> box = accountService.update(dto, principal.getName());
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

    /**
     * удаление аккаунта пользователя
     * запрос может выполнять только пользователь с ролью user
     * @param id
     * @param principal
     * @return
     * 200 - запрос успешно выполнен;
     * 403 - обновление не своего аккаунта, аккаунт не найден;
     * 401 - запрос от неваторизованного аккаунта, неверные авторизиационные данные;
     * 400 - неверные параметры запроса;
     */
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


    /**
     * валидация id
     * @param id
     * @return isValid value
     */
    private Boolean checkId(Integer id)
    {
        Boolean isAllowId =false;
        if (id!=null)
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
