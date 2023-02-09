package com.example.demo.service;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.dto.LocationPointDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface LocationPointService {

    Optional<LocationPointDto> findById(Long id);

    Optional<LocationPointDto> add(LocationPointDto dto) throws ResponseStatusException;

    Optional<LocationPointDto> update(LocationPointDto dto) throws ResponseStatusException;

    void remove(Long id) throws ResponseStatusException;

}
