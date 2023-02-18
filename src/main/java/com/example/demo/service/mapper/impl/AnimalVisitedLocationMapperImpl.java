package com.example.demo.service.mapper.impl;

import com.example.demo.model.AnimalVisitedLocation;
import com.example.demo.service.LocationPointService;
import com.example.demo.service.dto.AnimalVisitedLocationDto;
import com.example.demo.service.dto.LocationPointDto;
import com.example.demo.service.mapper.AnimalVisitedLocationMapper;
import com.example.demo.service.mapper.LocationPointMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * реализация интерфейса маппера AnimalVisitedLocationMapper
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Slf4j
@Service
public class AnimalVisitedLocationMapperImpl implements AnimalVisitedLocationMapper {


    private final LocationPointMapper locationPointMapper;


    private final LocationPointService locationPointService;

    @Autowired
    public AnimalVisitedLocationMapperImpl(LocationPointMapper locationPointMapper, LocationPointService locationPointService) {
        this.locationPointMapper = locationPointMapper;
        this.locationPointService = locationPointService;
    }

    @Transactional
    @Override
    public AnimalVisitedLocation toEntity(AnimalVisitedLocationDto dto) {
        log.info("animalVisitedLocation: convert dto to entity");
        AnimalVisitedLocation entity = new AnimalVisitedLocation();
        if (dto!=null)
        {
            if (dto.getId()!=null)
                entity.setId(dto.getId());
            if (dto.getLocationPointId()!=null)
            {
                Optional<LocationPointDto> box = locationPointService.findById(dto.getLocationPointId());
                if (box.isPresent())
                {
                    entity.setLocationPoint(locationPointMapper.toEntity(box.get()));
                }
            }
            if (dto.getDateTimeOfVisitLocationPoint()!=null)
                entity.setDateTimeOfVisitLocationPoint(dto.getDateTimeOfVisitLocationPoint());
        }
        return entity;

    }

    @Transactional
    @Override
    public AnimalVisitedLocationDto toDto(AnimalVisitedLocation entity) {
        log.info("animalVisitedLocation: convert entity to dto");
        AnimalVisitedLocationDto dto = new AnimalVisitedLocationDto();
        if (entity!=null)
        {
            if (entity.getId()!=null)
                dto.setId(entity.getId());
            if (entity.getLocationPoint()!=null)
                dto.setLocationPointId(entity.getLocationPoint().getId());
            if (entity.getDateTimeOfVisitLocationPoint()!=null)
                dto.setDateTimeOfVisitLocationPoint(entity.getDateTimeOfVisitLocationPoint());
        }
        return dto;
    }
}
