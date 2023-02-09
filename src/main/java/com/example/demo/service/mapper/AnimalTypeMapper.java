package com.example.demo.service.mapper;

import com.example.demo.model.AnimalType;
import com.example.demo.service.dto.AnimalTypeDto;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface AnimalTypeMapper {

    AnimalType toEntity(AnimalTypeDto dto);

    Set<AnimalType> toEntity(Set<AnimalTypeDto> dtoSet);

    AnimalTypeDto toDto(AnimalType entity);

    Set<AnimalTypeDto> toDto(Set<AnimalType> entitiesSet);

}
