package com.example.demo.service;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.dto.AccountViewDto;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {

    List<AccountViewDto> findAll();

    List<AccountViewDto> search(AccountDto dto, Pageable pageable);

    Optional<AccountViewDto> findById(Integer id);

    Optional<AccountDto> add(AccountDto dto, Principal principal) throws ResponseStatusException;

    Optional<AccountViewDto> update(AccountDto dto, String userName) throws ResponseStatusException;

    void remove(Integer id, String userName) throws ResponseStatusException;
}
