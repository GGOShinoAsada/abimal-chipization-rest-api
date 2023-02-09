package com.example.demo.service.mapper;

import com.example.demo.model.LocationPoint;
import com.example.demo.service.dto.LocationPointDto;
import org.springframework.stereotype.Service;

@Service
public interface LocationPointMapper {

    LocationPoint toEntity(LocationPointDto dto);

    LocationPointDto toDto(LocationPoint entity);

}
