package com.example.demo.service.impl;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.model.Account;
import com.example.demo.model.Animal;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public List<AccountDto> findAll() {
        log.info("get all accounts");
        List<AccountDto> list = new ArrayList();
        accountRepository.findAll().forEach(entity->list.add(accountMapper.toDto(entity)));
        return list;
    }

    @Transactional
    @Override
    public List<AccountDto> search(AccountDto dto, Pageable pageable) {
        log.info("search account by parameters: firstName = {}, lastName = {}, email = {}", dto.getFirstName(), dto.getLastName(), dto.getEmail());

        Page<Account> entities = accountRepository.findByFirstNameContainingAndLastNameContainingAndEmailContaining(dto.getFirstName(), dto.getLastName(), dto.getEmail(), pageable);
        List<AccountDto> dtoList = new ArrayList();
        if (entities.getSize()>0)
        {
            for (Account entity: entities.toList())
            {
                dtoList.add(accountMapper.toDto(entity));
            }
        }
        return dtoList;
    }

    @Transactional
    @Override
    public Optional<AccountDto> findById(Integer id) {
        log.info("find account with id {}",id);
        Optional<Account> entity = accountRepository.findById(id);
        if (entity.isPresent())
        {
            return Optional.of(accountMapper.toDto(entity.get()));
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
            isValid = !dto.getFirstName().isEmpty() && !dto.getLastName().isEmpty()
                    && !dto.getEmail().isEmpty() && !dto.getPassword().isEmpty();
            if (isValid)
            {
                isValid = !dto.getFirstName().contains(" ") && !dto.getLastName().contains(" ")
                        && !dto.getEmail().contains(" ") && !dto.getPassword().contains(" ");
            }
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
                String message = "firstName, lastName, email and password can't be empty and contains spaces";
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
    public Optional<AccountDto> update(AccountDto dto, String userName) throws ResponseStatusException{
        if (dto!=null && userName!=null)
        {
            log.info("update account entity {}", dto.toString());
            Boolean isValid = !dto.getFirstName().isEmpty() && !dto.getLastName().isEmpty()
                    && !dto.getEmail().isEmpty() && !dto.getPassword().isEmpty();
            if (isValid)
            {
                isValid = !dto.getFirstName().contains(" ") && !dto.getLastName().contains(" ") && !dto.getEmail().contains(" ") && !dto.getPassword().contains(" ");
                if (!isValid)
                {
                    String message = "firstName, lastName, email and password can't contains spaces";
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                Pattern pattern = Pattern.compile("^(.+)@(.+)$");
                Matcher matcher = pattern.matcher(dto.getEmail());
                isValid = matcher.matches();
                if (isValid)
                {
                    Optional<Account> box = accountRepository.findByEmail(userName);
                    if (box.isPresent())
                    {
                        Account entity = box.get();
                        isValid = entity.getEmail().equals(userName);
                        if (isValid)
                        {
                            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
                            entity = accountRepository.save(accountMapper.toEntity(dto));
                            return Optional.ofNullable(accountMapper.toDto(entity));
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
                String message = "firstName, lastName, email and password are mandatory";
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
            Optional<Account> box = accountRepository.findByEmail(userName);
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
                String message = "account with email "+userName+" was not found";
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
}
