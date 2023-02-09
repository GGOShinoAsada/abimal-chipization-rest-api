package com.example.demo.service.mapper;

import com.example.demo.model.Animal;
import com.example.demo.service.dto.AnimalDto;
import org.springframework.stereotype.Service;

@Service
public interface AnimalMapper {

    Animal toEntity(AnimalDto dto);

    AnimalDto toDto(Animal entity);
}
