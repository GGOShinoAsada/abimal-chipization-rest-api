package com.example.demo.service.mapper.impl;

import com.example.demo.model.Account;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.dto.AccountViewDto;
import com.example.demo.service.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * реализация интерфейса маппера AccountMapper
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Slf4j
@Service
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account toEntity(AccountDto dto) {
        log.info("account: convert dto to entity");
        Account entity = new Account();
        if (dto!=null)
        {
            if (dto.getId()!=null)
                entity.setId(dto.getId());
            if (dto.getFirstName()!=null)
                entity.setFirstName(dto.getFirstName());
            if (dto.getLastName()!=null)
                entity.setLastName(dto.getLastName());
            if (dto.getEmail()!=null)
                entity.setEmail(dto.getEmail());
            if (dto.getPassword()!=null)
                entity.setPassword(dto.getPassword());
        }
        return entity;
    }

    @Override
    public AccountDto toDto(Account entity) {
        log.info("account: convert entity to dto");
        AccountDto dto = new AccountDto();
        if (entity!=null)
        {
            if (entity.getId()!=null)
                dto.setId(entity.getId());
            if (entity.getFirstName()!=null)
                dto.setFirstName(entity.getFirstName());
            if (entity.getLastName()!=null)
                dto.setLastName(entity.getLastName());
            if (entity.getEmail()!=null)
                dto.setEmail(entity.getEmail());
            if (entity.getPassword()!=null)
                dto.setPassword(entity.getPassword());
        }
        return dto;
    }

    @Override
    public AccountViewDto convertEntityToViewDto(Account entity) {
        log.info("account: convert entity to viewDto");
        AccountViewDto viewDto = new AccountViewDto();
        if (entity!=null)
        {
            if (entity.getId()!=null)
            {
                viewDto.setId(entity.getId());
            }
            if (entity.getFirstName()!=null)
            {
                viewDto.setFirstName(entity.getFirstName());
            }
            if (entity.getLastName()!=null)
            {
                viewDto.setLastName(entity.getLastName());
            }
            if (entity.getEmail()!=null)
            {
                viewDto.setEmail(entity.getEmail());
            }
        }
        return viewDto;
    }

    @Override
    public AccountViewDto convertDtoToViewDto(AccountDto dto) {
        log.info("account: convert dto to viewDto");
        AccountViewDto viewDto = new AccountViewDto();
        if (dto!=null)
        {
            if (dto.getId()!=null)
            {
                viewDto.setId(dto.getId());
            }
            if (dto.getFirstName()!=null)
            {
                viewDto.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName()!=null)
            {
                viewDto.setLastName(dto.getLastName());
            }
            if (dto.getEmail()!=null)
            {
                viewDto.setEmail(dto.getEmail());
            }
        }
        return viewDto;
    }
}
