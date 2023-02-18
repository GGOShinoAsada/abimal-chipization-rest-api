package com.example.demo.config;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * сериализация и десериализация аккаунта
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Slf4j
@Component
public class AccountConverter implements Converter<String, Account> {


    private final AccountRepository accountRepository;

    @Autowired
    public AccountConverter(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account convert(String source) {
        log.info("get account information");
        Account entity = null;
        if (source!=null || !source.equals(""))
        {
            Optional<Account> account = Optional.empty();
            try{
                Integer id = Integer.parseInt(source);
                account = accountRepository.findById(id);
                if (account.isPresent())
                    entity = account.get();
            }
            catch (Exception e)
            {
                log.error(e.getStackTrace().toString());
            }
        }
        else {
            log.warn("input data is empty");
        }
        return entity;
    }
}
