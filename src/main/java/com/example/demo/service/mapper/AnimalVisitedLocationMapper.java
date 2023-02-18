package com.example.demo.service.mapper;

import com.example.demo.model.AnimalVisitedLocation;
import com.example.demo.service.dto.AnimalVisitedLocationDto;
import org.springframework.stereotype.Service;

/**
 * интерфейс, преобразующий AnimalVisitedLocation и AnimalVisitedLocationDto
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Service
public interface AnimalVisitedLocationMapper {

    /**
     * convert AnimalVisitedLocationDto to AnimalVisitedLocation
     * @param dto
     * @return AnimalVisitedLocation
     */
    AnimalVisitedLocation toEntity(AnimalVisitedLocationDto dto);

    /**
     * convert AnimalVisitedLocation to AnimalVisitedLocationDto
     * @param entity
     * @return AnimalVisitedLocationDto
     */
    AnimalVisitedLocationDto toDto(AnimalVisitedLocation entity);

}
