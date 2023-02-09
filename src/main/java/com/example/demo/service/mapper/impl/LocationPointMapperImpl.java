package com.example.demo.service.mapper.impl;

import com.example.demo.model.LocationPoint;
import com.example.demo.service.dto.LocationPointDto;
import com.example.demo.service.mapper.LocationPointMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LocationPointMapperImpl implements LocationPointMapper {

    @Override
    public LocationPoint toEntity(LocationPointDto dto) {
        log.info("locationPoint: convert dto to entity");
        LocationPoint entity =new LocationPoint();
        if (dto!=null)
        {
            if (dto.getId()!=null)
                entity.setId(dto.getId());
            if (dto.getLatitude()!=null)
                entity.setLatitude(dto.getLatitude());
            if (dto.getLongitude()!=null)
                entity.setLongitude(dto.getLongitude());
        }
        return entity;
    }

    @Override
    public LocationPointDto toDto(LocationPoint entity) {
        log.info("locationPoint: convert entity to dto");
        LocationPointDto dto = new LocationPointDto();
        if (entity!=null)
        {
            if (entity.getId()!=null)
                dto.setId(entity.getId());
            if (entity.getLatitude()!=null)
                dto.setLongitude(entity.getLongitude());
            if (entity.getLongitude()!=null)
                dto.setLatitude(entity.getLatitude());
        }
        return dto;
    }
}
