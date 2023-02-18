package com.example.demo.service.mapper;

import com.example.demo.model.AnimalType;
import com.example.demo.service.dto.AnimalTypeDto;
import org.springframework.stereotype.Service;
import java.util.Set;

/**
 * интерфейс, преобразующий AnimalType и AnimalTypeDto
 * @author ROMAN
 * @date 2023-20-17
 * @version 1.0
 */
@Service
public interface AnimalTypeMapper {

    /**
     * convert AnimalTypeDto to AnimalType
     * @param dto
     * @return AnimalType
     */
    AnimalType toEntity(AnimalTypeDto dto);

    /**
     * convert set of AnimalTypeDto to set of AnimalType
     * @param dtoSet
     * @return set of AnimalType
     */
    Set<AnimalType> toEntity(Set<AnimalTypeDto> dtoSet);

    /**
     * convert AnimalType to AnimalTypeDto
     * @param entity
     * @return AnimalTypeDto
     */
    AnimalTypeDto toDto(AnimalType entity);

    /**
     * convert AnimalTypeDto to AnimalType
     * @param entitiesSet
     * @return set of AnimalTypeDto
     */
    Set<AnimalTypeDto> toDto(Set<AnimalType> entitiesSet);

}
