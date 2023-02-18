package com.example.demo.service;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.dto.AnimalVisitedLocationDto;
import com.example.demo.service.dto.AnimalVisitedLocationSearchDto;
import com.example.demo.service.dto.AnimalVisitedLocationUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * интерфейс, определяющий бизнес логику сущности AnimalVisitedLocation
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Service
public interface AnimalVisitedLocationService {

    /**
     * поиск точек локации, посущенных животными по параметрам
     * @param id
     * @param dto
     * @param pageable
     * @return list of AnimalVisitedLocationDto or empty list
     * @throws ResponseStatusException
     */
    List<AnimalVisitedLocationDto> findAll(Long id, AnimalVisitedLocationSearchDto dto, Pageable pageable) throws ResponseStatusException;

    /**
     * получение точки локации, посещенной животным, по id
     * @param id
     * @return AnimalVisitedLocationDto or null
     */
    Optional<AnimalVisitedLocationDto> findById(Long id);

    /**
     * добавление новой точки локации, посещенной животным
     * @param animalId
     * @param pointId
     * @return AnimalVisitedLocationDto or empty list
     * @throws ResponseStatusException
     */
    Optional<AnimalVisitedLocationDto> add (Long animalId, Long pointId) throws ResponseStatusException;

    /**
     * обновление точки локации, посещеной животным
     * @param animalId
     * @param dto
     * @return AnimalVisitedLocationDto or null
     * @throws ResponseStatusException
     */
    Optional<AnimalVisitedLocationDto> update(Long animalId, AnimalVisitedLocationUpdateDto dto) throws ResponseStatusException;

    /**
     * удаление точки локации, посещенной животным
     * @param animalId
     * @param pointId
     * @throws ResponseStatusException
     */
    void remove(Long animalId, Long pointId) throws ResponseStatusException;


}
