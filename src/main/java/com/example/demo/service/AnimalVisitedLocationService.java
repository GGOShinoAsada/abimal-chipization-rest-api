package com.example.demo.service;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.dto.AnimalVisitedLocationDto;
import com.example.demo.service.dto.AnimalVisitedLocationSearchDto;
import com.example.demo.service.dto.AnimalVisitedLocationUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AnimalVisitedLocationService {

    List<AnimalVisitedLocationDto> findAll(Long id, AnimalVisitedLocationSearchDto dto, Pageable pageable) throws ResponseStatusException;

    Optional<AnimalVisitedLocationDto> findById(Long id);

    Optional<AnimalVisitedLocationDto> add (Long animalId, Long pointId) throws ResponseStatusException;

    Optional<AnimalVisitedLocationDto> update(Long animalId, AnimalVisitedLocationUpdateDto dto) throws ResponseStatusException;

    void remove(Long animalId, Long pointId) throws ResponseStatusException;


}
