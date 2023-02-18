package com.example.demo.service.impl;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.model.Account;
import com.example.demo.model.Animal;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.dto.AccountViewDto;
import com.example.demo.service.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * реализация бизнес логики интерфейса AccountService
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;

    private final AnimalRepository animalRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AnimalRepository animalRepository, BCryptPasswordEncoder passwordEncoder, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.animalRepository = animalRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountMapper = accountMapper;
    }

    @Transactional
    @Override
    public List<AccountViewDto> findAll() {
        log.info("get all accounts");
        List<AccountViewDto> list = new ArrayList();
        accountRepository.findAll().forEach(entity->list.add(accountMapper.convertEntityToViewDto(entity)));
        return list;
    }

    @Transactional
    @Override
    public List<AccountViewDto> search(String username, AccountDto dto, Pageable pageable) {
        log.info("search account by parameters");
        Page<Account> entities = Page.empty();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending());
        if (dto.getFirstName()!=null && dto.getLastName()!=null && dto.getEmail()!=null)
        {

            entities = accountRepository.findByFirstNameContainingAndLastNameContainingAndEmailContaining(dto.getFirstName(), dto.getLastName(), dto.getEmail(), pageable);
        }
        else
        {
            entities = accountRepository.findAll(pageable);
        }
        List<AccountViewDto> dtoList = new ArrayList();
        if (entities.getSize()>0)
        {
            for (Account entity: entities.toList())
            {
                if (entity.getEmail().equals(username))
                    dtoList.add(accountMapper.convertEntityToViewDto(entity));
            }
        }
        return dtoList;
    }

    @Transactional
    @Override
    public Optional<AccountViewDto> findById(String username, Integer id) {
        log.info("find account with id {}",id);
        Optional<Account> box = accountRepository.findById(id);
        if (box.isPresent())
        {
            Account entity = box.get();
            if (entity.getEmail().equals(username))
            {
                return Optional.of(accountMapper.convertEntityToViewDto(box.get()));
            }
            else
            {
                log.warn("found account does not belong to you");
                return Optional.empty();
            }
        }
        else {
            String message = "account with is "+id+" was not found";
            log.warn(message);
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<AccountDto> add(AccountDto dto, Principal principal) throws ResponseStatusException {
        if (dto!=null)
        {
            log.info("adding account entity ", dto.toString());
            Boolean isValid = principal == null;
            if (!isValid)
            {
                String message = "you are already authorized in system";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
            }
            isValid = isAllowValue(dto.getFirstName()) && isAllowValue(dto.getLastName()) && isAllowValue(dto.getEmail()) && isAllowValue(dto.getPassword());
            if (isValid)
            {

                isValid = isAllowEmail(dto.getEmail());
                if (isValid)
                {
                    isValid = !accountRepository.findByEmail(dto.getEmail()).isPresent();
                    if (isValid)
                    {
                        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
                        dto = accountMapper.toDto(accountRepository.save(accountMapper.toEntity(dto)));
                        return Optional.ofNullable(dto);
                    }
                    else
                    {
                        String message = "email "+dto.getEmail()+" is already register in system";
                        throw new ResponseStatusException(HttpStatus.CONFLICT, message);
                    }
                }
                else
                {
                    String message = "email syntax is invalid";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);

                }

            }
            else
            {
                String message = "firstName, lastName, email and password are mandatory and can't contains spaces and service symbols";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
            }

        }
        else {
            String message = "input entity is null";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Transactional
    @Override
    public Optional<AccountViewDto> update(AccountDto dto, String userName) throws ResponseStatusException{
        if (dto!=null && userName!=null)
        {
            log.info("update account entity {}", dto.toString());
            Boolean isValid = isAllowValue(dto.getFirstName()) && isAllowValue(dto.getLastName()) && isAllowValue(dto.getEmail()) && isAllowValue(dto.getPassword());
            if (isValid)
            {
                isValid = isAllowEmail(dto.getEmail());
                if (isValid)
                {
                    Optional<Account> box = accountRepository.findById(dto.getId());
                    if (box.isPresent())
                    {
                        Account entity = box.get();
                        isValid = entity.getEmail().equals(userName);
                        if (isValid)
                        {
                            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
                            entity = accountRepository.save(accountMapper.toEntity(dto));
                            return Optional.ofNullable(accountMapper.convertEntityToViewDto(entity));
                        }
                        else
                        {
                            String message = "updating not yourself account";
                            log.warn(message);
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
                        }
                    }
                    else
                    {
                        String message = "account with email "+userName+" was not found";
                        log.warn(message);
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
                    }

                }
                else
                {
                    String message = "email syntax is invalid";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);

                }
            }
            else
            {
                String message = "firstName, lastName, email and password are mandatory and can't contains spaces and service symbols";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
            }
        }
        else {
            String message = "input entity is empty";
            log.error(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Transactional
    @Override
    public void remove(Integer id, String userName) throws ResponseStatusException {
        log.info("remove account with id {}", id);
        if (id!=null && userName!=null)
        {
            Optional<Account> box = accountRepository.findById(id);
            if (box.isPresent())
            {
                Account entity = box.get();
                if (entity.getEmail().equals(userName))
                {
                    Optional<Animal> animalBox = animalRepository.findByChipperId(id);
                    if (!animalBox.isPresent())
                    {
                        accountRepository.deleteById(id);
                        log.info("account removing success");
                    }
                    else
                    {
                        String message = "account with id "+id+" use in animal entity, id"+animalBox.get().getId();
                        log.warn(message);
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                    }
                }
                else
                {
                    String message = "updating not yourself account";
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
                }
            }
            else
            {
                String message = "account with id "+id+" was not found";
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
            }
        }
        else
        {
            String message = "id and username are mandatory";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

   private Boolean isAllowValue(String value)
   {
       Boolean flag = false;
       if (!value.isEmpty())
       {
           flag = !value.contains(" ") && !value.contains("\n") && !value.contains("\t");
       }
       return flag;
   }

   private Boolean isAllowEmail(String email)
   {
       Boolean flag = false;
       if (!email.isEmpty())
       {
           Pattern pattern = Pattern.compile("^(.+)@(.+)$");
           Matcher matcher = pattern.matcher(email);
           flag = matcher.matches();
       }
       return flag;
   }
}
