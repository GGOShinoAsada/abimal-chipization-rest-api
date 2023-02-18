package com.example.demo.service;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.dto.LocationPointDto;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * интерфейс, определяющий бизнес логику для сущности LocationPoint
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Service
public interface LocationPointService {

    /**
     * поиск точки локации по id
     * @param id
     * @return LocationPointDto or null
     */
    Optional<LocationPointDto> findById(Long id);

    /**
     * добавление точки локации
     * @param dto
     * @return LocationPointDto or null
     * @throws ResponseStatusException
     */
    Optional<LocationPointDto> add(LocationPointDto dto) throws ResponseStatusException;

    /**
     * обновление точки локации
     * @param dto
     * @return LocationPointDto or null
     * @throws ResponseStatusException
     */
    Optional<LocationPointDto> update(LocationPointDto dto) throws ResponseStatusException;

    /**
     * удаление точки локации
     * @param id
     * @throws ResponseStatusException
     */
    void remove(Long id) throws ResponseStatusException;

}
