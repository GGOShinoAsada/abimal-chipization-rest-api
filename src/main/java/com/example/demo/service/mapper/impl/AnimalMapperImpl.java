package com.example.demo.service.mapper.impl;

import com.example.demo.model.Animal;
import com.example.demo.model.AnimalType;
import com.example.demo.model.AnimalVisitedLocation;
import com.example.demo.service.AnimalTypeService;
import com.example.demo.service.AnimalVisitedLocationService;
import com.example.demo.service.LocationPointService;
import com.example.demo.service.dto.AnimalDto;
import com.example.demo.service.dto.AnimalTypeDto;
import com.example.demo.service.dto.AnimalVisitedLocationDto;
import com.example.demo.service.dto.LocationPointDto;
import com.example.demo.service.mapper.AnimalMapper;
import com.example.demo.service.mapper.AnimalTypeMapper;
import com.example.demo.service.mapper.AnimalVisitedLocationMapper;
import com.example.demo.service.mapper.LocationPointMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class AnimalMapperImpl implements AnimalMapper {


    private final AnimalTypeService animalTypeService;

    private final LocationPointService locationPointService;

    private final AnimalVisitedLocationService animalVisitedLocationService;

    private final AnimalTypeMapper animalTypeMapper;

    private final LocationPointMapper locationPointMapper;

    private final AnimalVisitedLocationMapper animalVisitedLocationMapper;

    @Autowired
    public AnimalMapperImpl(AnimalTypeService animalTypeService, LocationPointService locationPointService, AnimalVisitedLocationService animalVisitedLocationService, AnimalTypeMapper animalTypeMapper, LocationPointMapper locationPointMapper, AnimalVisitedLocationMapper animalVisitedLocationMapper) {
        this.animalTypeService = animalTypeService;
        this.locationPointService = locationPointService;
        this.animalVisitedLocationService = animalVisitedLocationService;
        this.animalTypeMapper = animalTypeMapper;
        this.locationPointMapper = locationPointMapper;
        this.animalVisitedLocationMapper = animalVisitedLocationMapper;
    }

    @Transactional
    @Override
    public Animal toEntity(AnimalDto dto) {
        log.info("animal: convert dto to entity");
        Animal entity = new Animal();
        if (dto!=null)
        {
            if (dto.getId()!=null)
                entity.setId(dto.getId());
            if (dto.getAnimalTypes()!=null)
            {
                Set<AnimalType> animalTypes = new HashSet();
                for (Long id: dto.getAnimalTypes())
                {
                    Optional<AnimalTypeDto> box = animalTypeService.findById(id);
                    if (box.isPresent())
                    {
                        animalTypes.add(animalTypeMapper.toEntity(box.get()));
                    }
                }
                entity.setAnimalTypes(animalTypes);
            }
            if (dto.getWeight()!=null)
                entity.setWeight(dto.getWeight());
            if (dto.getHeight()!=null)
                entity.setHeight(dto.getHeight());
            if (dto.getLength()!=null)
                entity.setLength(dto.getLength());
            if (dto.getGender()!=null)
                entity.setGender(dto.getGender());
            if (dto.getLifeStatus()!=null)
                entity.setLifeStatus(dto.getLifeStatus());
            if (dto.getChippingDateTime()!=null)
                entity.setChippingDateTime(dto.getChippingDateTime());
            if (dto.getChipperId()!=null)
                entity.setChipperId(dto.getChipperId());
            if (dto.getChippingLocationId()!=null)
            {
                Optional<LocationPointDto> box =  locationPointService.findById(dto.getChippingLocationId());
                if (box.isPresent())
                {
                    entity.setChippingLocationId(locationPointMapper.toEntity(box.get()));
                }
            }
            if (dto.getVisitedLocations()!=null)
            {
                List<AnimalVisitedLocation> chippingLocations = new ArrayList();
                for (Long id: dto.getVisitedLocations())
                {
                    Optional<AnimalVisitedLocationDto> animalVisitedLocationBox = animalVisitedLocationService.findById(id);
                    if (animalVisitedLocationBox.isPresent())
                    {
                        chippingLocations.add(animalVisitedLocationMapper.toEntity(animalVisitedLocationBox.get()));
                    }
                }
                entity.setAnimalVisitedLocations(chippingLocations);
            }
            if (dto.getDeathDateTime()!=null)
                entity.setDeathDatetime(dto.getDeathDateTime());
        }
        return entity;
    }

    @Transactional
    @Override
    public AnimalDto toDto(Animal entity) {
        log.info("animal: convert dto to entity");
        AnimalDto dto = new AnimalDto();
        if (entity!=null)
        {
            if (entity.getId()!=null)
                dto.setId(entity.getId());
            if (entity.getAnimalTypes()!=null)
            {
                Set<Long> animalTypes = new HashSet();
                for (AnimalType animalType: entity.getAnimalTypes())
                {
                    animalTypes.add(animalType.getId());
                }
                dto.setAnimalTypes(animalTypes);
            }
            if (entity.getWeight()!=null)
                dto.setWeight(entity.getWeight());
            if (entity.getHeight()!=null)
                dto.setHeight(entity.getHeight());
            if (entity.getLength()!=null)
                dto.setLength(entity.getLength());
            if (entity.getGender()!=null)
                dto.setGender(entity.getGender());
            if (entity.getLifeStatus()!=null)
                dto.setLifeStatus(entity.getLifeStatus());
            if (entity.getChippingDateTime()!=null)
                dto.setChippingDateTime(entity.getChippingDateTime());
            if (entity.getChipperId()!=null)
                dto.setChipperId(entity.getChipperId());
            if (entity.getChippingLocationId()!=null)
            {
                dto.setChippingLocationId(entity.getChippingLocationId().getId());
            }
            if (entity.getAnimalVisitedLocations()!=null)
            {
                List<Long> animalVisitedLocationIds = new ArrayList();
                for (AnimalVisitedLocation item: entity.getAnimalVisitedLocations())
                {
                    animalVisitedLocationIds.add(item.getId());
                }
                dto.setVisitedLocations(animalVisitedLocationIds);
            }
            if (entity.getDeathDatetime()!=null)
                dto.setDeathDateTime(entity.getDeathDatetime());

        }
        return dto;
    }
}
