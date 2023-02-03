package com.example.demo.service.impl;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {


    private final AccountRepository repository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<AccountDto> findAll() {
        log.info("get all accounts");
        List<AccountDto> list = new ArrayList();
        repository.findAll().forEach(entity->list.add(AccountMapper.toDto(entity)));
        return list;
    }

    @Override
    public Optional<AccountDto> findById(Integer id) {
        log.info("find account with id {}",id);
        Optional<Account> entity = repository.findById(id);
        if (entity.isPresent())
        {
            return Optional.of(AccountMapper.toDto(entity.get()));
        }
        else {
            log.warn("account not found");
            return Optional.empty();
        }
    }

    @Override
    public Optional<AccountDto> add(AccountDto dto) {
        if (dto!=null)
        {
            log.info("add account entity ", dto.toString());
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
            dto = AccountMapper.toDto(repository.save(AccountMapper.toEntity(dto)));
            return Optional.of(dto);
        }
        else {
            log.error("input entity is null");
        }
        return Optional.empty();
    }

    @Override
    public Optional<AccountDto> update(AccountDto dto) {
        if (dto!=null)
        {
            log.info("update account entity {}", dto.toString());
            Optional<Account> entity = repository.findById(dto.getId());
            if (entity.isPresent())
            {
                entity = Optional.of(AccountMapper.toEntity(dto));
                entity = Optional.of(repository.save(entity.get()));
                return Optional.of(AccountMapper.toDto(entity.get()));
            }
            else {
                log.warn("account not found");
                return Optional.empty();
            }
        }
        else {
            log.error("input entity is empty");
            return Optional.empty();
        }
    }

    @Override
    public void remove(Integer id) {
        log.info("remove account with id {}", id);
        repository.deleteById(id);
    }
}
