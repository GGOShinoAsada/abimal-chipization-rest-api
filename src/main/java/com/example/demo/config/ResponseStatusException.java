package com.example.demo.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * пользовательское исключение неверного запроса
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Slf4j
@Getter
public class ResponseStatusException extends Exception{

    private HttpStatus statusCode;

    public ResponseStatusException(HttpStatus statusCode, String message)
    {
        super(message);
        log.info("initialize responseStatusException with statusCode "+statusCode.name());
        this.statusCode = statusCode;
    }
}
