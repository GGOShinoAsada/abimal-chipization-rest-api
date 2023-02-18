package com.example.demo.service.mapper;

import com.example.demo.model.Animal;
import com.example.demo.service.dto.AnimalDto;
import org.springframework.stereotype.Service;

/**
 * интерфейс, преобразующий сущности Animal и AnimalDto
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Service
public interface AnimalMapper {

    /**
     * convert AnimalDto to Animal
     * @param dto
     * @return Animal
     */
    Animal toEntity(AnimalDto dto);

    /**
     * convert Animal to AnimalDto
     * @param entity
     * @return AnimalDto
     */
    AnimalDto toDto(Animal entity);
}
