package com.example.demo.service.mapper.impl;

import com.example.demo.model.Account;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.dto.AccountViewDto;
import com.example.demo.service.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            if (!dto.getFirstName().isEmpty())
                entity.setFirstName(dto.getFirstName());
            if (!dto.getLastName().isEmpty())
                entity.setLastName(dto.getLastName());
            if (!dto.getEmail().isEmpty())
                entity.setEmail(dto.getEmail());
            if (!dto.getPassword().isEmpty())
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
            if (!entity.getFirstName().isEmpty())
                dto.setFirstName(entity.getFirstName());
            if (!entity.getLastName().isEmpty())
                dto.setLastName(entity.getLastName());
            if (!entity.getEmail().isEmpty())
                dto.setEmail(entity.getEmail());
            if (!entity.getPassword().isEmpty())
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
            if (!entity.getFirstName().isEmpty())
            {
                viewDto.setFirstName(entity.getFirstName());
            }
            if (!entity.getLastName().isEmpty())
            {
                viewDto.setLastName(entity.getLastName());
            }
            if (!entity.getEmail().isEmpty())
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
            if (!dto.getFirstName().isEmpty())
            {
                viewDto.setFirstName(dto.getFirstName());
            }
            if (!dto.getLastName().isEmpty())
            {
                viewDto.setLastName(dto.getLastName());
            }
            if (!dto.getEmail().isEmpty())
            {
                viewDto.setEmail(dto.getEmail());
            }
        }
        return viewDto;
    }
}
