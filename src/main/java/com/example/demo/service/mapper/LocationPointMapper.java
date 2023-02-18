package com.example.demo.service.mapper;

import com.example.demo.model.LocationPoint;
import com.example.demo.service.dto.LocationPointDto;
import org.springframework.stereotype.Service;

/**
 * интерфейс, преобразующий LocationPoint и LocationPointDto
 */
@Service
public interface LocationPointMapper {

    /**
     * convert LocationPointDto to LocationPoint
     * @param dto
     * @return LocationPoint
     */
    LocationPoint toEntity(LocationPointDto dto);

    /**
     * convert LocationPoint to LocationPointDto
     * @param entity
     * @return LocationPoint
     */
    LocationPointDto toDto(LocationPoint entity);

}
