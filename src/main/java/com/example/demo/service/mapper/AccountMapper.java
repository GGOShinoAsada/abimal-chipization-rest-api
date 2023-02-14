package com.example.demo.service.mapper;

import com.example.demo.model.Account;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.dto.AccountViewDto;
import org.springframework.stereotype.Service;


@Service
public interface AccountMapper
{
    Account toEntity(AccountDto dto);

    AccountDto toDto(Account entity);

    AccountViewDto convertEntityToViewDto(Account entity);

    AccountViewDto convertDtoToViewDto(AccountDto dto);
}