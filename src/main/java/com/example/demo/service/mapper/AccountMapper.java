package com.example.demo.service.mapper;

import com.example.demo.model.Account;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.dto.AccountViewDto;
import org.springframework.stereotype.Service;

/**
 * интерфейс, преобразующий сущности Account, AccountViewDto и AccountDto
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Service
public interface AccountMapper
{
    /**
     * convert AccountDto to Account
     * @param dto
     * @return Account
     */
    Account toEntity(AccountDto dto);

    /**
     * convert Account to AccountDto
     * @param entity
     * @return AccountDto
     */
    AccountDto toDto(Account entity);

    /**
     * convert Account to AccountViewDto
     * @param entity
     * @return AccountViewDto
     */
    AccountViewDto convertEntityToViewDto(Account entity);

    /**
     * convert AccountDto to AccountViewDto
     * @param dto
     * @return AccountViewDto
     */
    AccountViewDto convertDtoToViewDto(AccountDto dto);
}