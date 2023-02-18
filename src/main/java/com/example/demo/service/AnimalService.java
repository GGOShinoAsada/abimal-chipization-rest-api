package com.example.demo.service;


import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.dto.AnimalDto;
import com.example.demo.service.dto.AnimalSearchDto;
import com.example.demo.service.dto.AnimalTypeUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * интерфейс, определяющий бизнес логику сущности Animal
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Service
public interface AnimalService {

    /**
     * получение животного по id
     * @param id
     * @return AnimalDto or null
     */
    Optional<AnimalDto> findById(Long id);

    /**
     * поиск животных по параметрам
     * @param dto
     * @param pageable
     * @return list of AnimalDto or empty list
     * @throws ResponseStatusException
     */
    List<AnimalDto> search(AnimalSearchDto dto, Pageable pageable) throws ResponseStatusException;

    /**
     * добавление нового животного
     * @param dto
     * @return AnimalDto or null
     * @throws ResponseStatusException
     */
    Optional<AnimalDto> add(AnimalDto dto) throws ResponseStatusException;

    /**
     * обновление информации о животном
     * @param dto
     * @return AnimalDto or null
     * @throws ResponseStatusException
     */
    Optional<AnimalDto> update(AnimalDto dto) throws ResponseStatusException;

    /**
     * удаление животного
     * @param id
     * @throws ResponseStatusException
     */
    void remove(Long id) throws ResponseStatusException;

    /**
     * добавление типа животного к животному
     * @param animalId
     * @param typeId
     * @return AnimalDto or null
     * @throws ResponseStatusException
     */
    Optional<AnimalDto> addAnimalTypeToAnimal(Long animalId, Long typeId) throws ResponseStatusException;

    /**
     * обновление типа животного для животного
     * @param animalId
     * @param dto
     * @return AnimalDto or null
     * @throws ResponseStatusException
     */
    Optional<AnimalDto> updateAnimalTypeForAnimal(Long animalId, AnimalTypeUpdateDto dto) throws ResponseStatusException;

    /**
     * удаление типа животного для животного
     * @param animalId
     * @param typeId
     * @return
     * @throws ResponseStatusException
     */
    Optional<AnimalDto> removeAnimalTypeForAnimal(Long animalId, Long typeId) throws ResponseStatusException;

}
