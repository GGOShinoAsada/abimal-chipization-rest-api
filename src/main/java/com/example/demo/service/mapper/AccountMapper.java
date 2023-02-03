package com.example.demo.service.mapper;

import com.example.demo.model.Account;
import com.example.demo.service.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountMapper {
    public static Account toEntity(AccountDto dto)
    {
        log.info("Account: mapping dto {} to entity", dto.toString());
        Account entity = new Account();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        return entity;
    }

    public static AccountDto toDto(Account entity)
    {
        log.info("Account: mapping entity {} to dto", entity.toString());
        AccountDto dto = new AccountDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        return dto;
    }
}
