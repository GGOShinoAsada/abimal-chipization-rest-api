package com.example.demo.service;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.dto.AccountDto;
import com.example.demo.service.dto.AccountViewDto;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * интерфейс, определяющий бизнес логику для сущности Account
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Service
public interface AccountService {

    /**
     * возвращает все аккаунты
     * @return list of AccountViewDto or empty list
     */
    List<AccountViewDto> findAll();

    /**
     * выполняет поиск аккаунта по параметрам
     * @param username
     * @param dto
     * @param pageable
     * @return list of AccountViewDto or empty list
     */
    List<AccountViewDto> search(String username, AccountDto dto, Pageable pageable);

    /**
     * осуществляет поиск аккаунта по id
      * @param username
     * @param id
     * @return AccountViewDto
     */
    Optional<AccountViewDto> findById(String username, Integer id);

    /**
     * добавляет новый аккаунт
     * @param dto
     * @param principal
     * @return AccountDto or null
     * @throws ResponseStatusException
     */
    Optional<AccountDto> add(AccountDto dto, Principal principal) throws ResponseStatusException;

    /**
     * обновляет информацию об аккаунте
     * @param dto
     * @param userName
     * @return AccountViewDto or null
     * @throws ResponseStatusException
     */
    Optional<AccountViewDto> update(AccountDto dto, String userName) throws ResponseStatusException;

    /**
     * удаляет аккаунт
     * @param id
     * @param userName
     * @throws ResponseStatusException
     */
    void remove(Integer id, String userName) throws ResponseStatusException;
}
