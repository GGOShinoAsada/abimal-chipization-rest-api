package com.example.demo.service.mapper;

import com.example.demo.model.AnimalVisitedLocation;
import com.example.demo.service.dto.AnimalVisitedLocationDto;
import org.springframework.stereotype.Service;

@Service
public interface AnimalVisitedLocationMapper {

    AnimalVisitedLocation toEntity(AnimalVisitedLocationDto dto);

    AnimalVisitedLocationDto toDto(AnimalVisitedLocation entity);

}
