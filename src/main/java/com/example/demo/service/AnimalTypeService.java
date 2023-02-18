package com.example.demo.service;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.dto.AnimalTypeDto;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * интерфейс, определяющий бизнес логику сущности AnimalType
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Service
public interface AnimalTypeService {

    /**
     * получение животного по id
     * @param id
     * @return AnimalTypeDto or null
     */
    Optional<AnimalTypeDto> findById(Long id);

    /**
     * добавление типа животного
     * @param dto
     * @return AnimalTypeDto or null
     * @throws ResponseStatusException
     */
    Optional<AnimalTypeDto> add(AnimalTypeDto dto) throws ResponseStatusException;

    /**
     * обновление типа животного
     * @param dto
     * @return AnimalTypeDto or null
     * @throws ResponseStatusException
     */
    Optional<AnimalTypeDto> update(AnimalTypeDto dto) throws ResponseStatusException;

    /**
     * удаление типа животного
     * @param id
     * @throws ResponseStatusException
     */
    void remove(Long id) throws ResponseStatusException;

}
