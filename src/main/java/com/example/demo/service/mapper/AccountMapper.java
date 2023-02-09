package com.example.demo.service.mapper;

import com.example.demo.model.Account;
import com.example.demo.service.dto.AccountDto;
import org.springframework.stereotype.Service;


@Service
public interface AccountMapper
{
    Account toEntity(AccountDto dto);

    AccountDto toDto(Account entity);

}