package com.example.demo.service;


import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.dto.AnimalDto;
import com.example.demo.service.dto.AnimalSearchDto;
import com.example.demo.service.dto.AnimalTypeDto;
import com.example.demo.service.dto.AnimalTypeUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public interface AnimalService {

    Optional<AnimalDto> findById(Long id);

    List<AnimalDto> search(AnimalSearchDto dto, Pageable pageable) throws ResponseStatusException;

    Optional<AnimalDto> add(AnimalDto dto) throws ResponseStatusException;

    Optional<AnimalDto> update(AnimalDto dto) throws ResponseStatusException;

    void remove(Long id) throws ResponseStatusException;

    Optional<AnimalDto> addAnimalTypeToAnimal(Long animalId, Long typeId) throws ResponseStatusException;

    Optional<AnimalDto> updateAnimalTypeForAnimal(Long animalId, AnimalTypeUpdateDto dto) throws ResponseStatusException;

    Optional<AnimalDto> removeAnimalTypeForAnimal(Long animalId, Long typeId) throws ResponseStatusException;

}
