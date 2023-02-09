package com.example.demo.service;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.dto.AnimalTypeDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AnimalTypeService {

    Optional<AnimalTypeDto> findById(Long id);

    Optional<AnimalTypeDto> add(AnimalTypeDto dto) throws ResponseStatusException;

    Optional<AnimalTypeDto> update(AnimalTypeDto dto) throws ResponseStatusException;

    void remove(Long id) throws ResponseStatusException;

}
