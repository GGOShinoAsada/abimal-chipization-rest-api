package com.example.demo.service.mapper.impl;

import com.example.demo.model.AnimalType;
import com.example.demo.service.dto.AnimalTypeDto;
import com.example.demo.service.mapper.AnimalTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class AnimalTypeMapperImpl implements AnimalTypeMapper {

    @Override
    public AnimalType toEntity(AnimalTypeDto dto) {
        log.info("animalType: convert dto to entity");
        AnimalType entity = new AnimalType();
        if (dto!=null)
        {
            if (dto.getId()!=null)
                entity.setId(dto.getId());
            if (!dto.getType().isEmpty())
                entity.setType(dto.getType());
        }
        return entity;

    }

    @Override
    public Set<AnimalType> toEntity(Set<AnimalTypeDto> dtoSet) {
        log.info("animalType: convert set of dto to set of entity");
        Set<AnimalType> entities = new HashSet();
        if (dtoSet!=null)
        {
            for (AnimalTypeDto dto: dtoSet)
            {
                entities.add(toEntity(dto));
            }
        }
        return entities;
    }

    @Override
    public AnimalTypeDto toDto(AnimalType entity) {
        log.info("animalType: convert entity to dto");
        AnimalTypeDto dto = new AnimalTypeDto();
        if (entity!=null)
        {
            if (entity.getId()!=null)
                dto.setId(entity.getId());
            if (!entity.getType().isEmpty())
                dto.setType(entity.getType());
        }
        return dto;
    }

    @Override
    public Set<AnimalTypeDto> toDto(Set<AnimalType> entitiesSet) {
        log.info("animalType: convert set of dto to set of entities");
        Set<AnimalTypeDto> dtoSet = new HashSet();
        if (entitiesSet!=null)
        {
            for (AnimalType entity: entitiesSet)
            {
                dtoSet.add(toDto(entity));
            }
        }
        return null;
    }
}
