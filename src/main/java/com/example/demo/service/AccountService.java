package com.example.demo.service;

import com.example.demo.service.dto.AccountDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {

    List<AccountDto> findAll();

    Optional<AccountDto> findById(Integer id);

    Optional<AccountDto> add(AccountDto dto);

    Optional<AccountDto> update(AccountDto dto);

    void remove(Integer id);
}
