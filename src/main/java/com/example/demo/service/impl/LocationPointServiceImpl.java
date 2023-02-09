package com.example.demo.service.impl;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.model.Animal;
import com.example.demo.model.LocationPoint;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.repository.AnimalVisitedLocationRepository;
import com.example.demo.repository.LocationPointRepository;
import com.example.demo.service.LocationPointService;
import com.example.demo.service.dto.LocationPointDto;
import com.example.demo.service.mapper.LocationPointMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class LocationPointServiceImpl implements LocationPointService {

    private LocationPointRepository locationPointRepository;

    private AnimalRepository animalRepository;

    private AnimalVisitedLocationRepository animalVisitedLocationRepository;

    private LocationPointMapper locationPointMapper;

    @Autowired
    public void setLocationPointRepository(LocationPointRepository locationPointRepository) {
        this.locationPointRepository = locationPointRepository;
    }

    @Autowired
    public void setAnimalRepository(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Autowired
    public void setAnimalVisitedLocationRepository(AnimalVisitedLocationRepository animalVisitedLocationRepository) {
        this.animalVisitedLocationRepository = animalVisitedLocationRepository;
    }

    @Autowired
    public void setLocationPointMapper(LocationPointMapper locationPointMapper) {
        this.locationPointMapper = locationPointMapper;
    }

    /*@Autowired
    public LocationPointServiceImpl( LocationPointRepository locationPointRepository, AnimalRepository animalRepository, AnimalVisitedLocationRepository animalVisitedLocationRepository, LocationPointMapper locationPointMapper) {
        this.locationPointRepository = locationPointRepository;
        this.animalRepository = animalRepository;
        this.animalVisitedLocationRepository = animalVisitedLocationRepository;
        this.locationPointMapper = locationPointMapper;
    }*/

    @Transactional
    @Override
    public Optional<LocationPointDto> findById(Long id) {
        log.info("get location point by id "+id);
        Optional<LocationPoint> box = locationPointRepository.findById(id);
        if (box.isPresent())
        {
            return Optional.of(locationPointMapper.toDto(box.get()));
        }
        else
        {
            log.warn("location point was not found");
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<LocationPointDto> add(LocationPointDto dto) throws ResponseStatusException {
        log.info("add new location point");
        if (dto!=null)
        {
            Optional<LocationPoint> box = locationPointRepository.findByLatitudeAndLongitude(dto.getLatitude(), dto.getLongitude());
            if (!box.isPresent())
            {
                LocationPoint entity = locationPointRepository.save(locationPointMapper.toEntity(dto));
                log.info("adding locationPoint success");
                return Optional.ofNullable(locationPointMapper.toDto(entity));
            }
            else
            {
                String message = "locationPoint with latitude "+dto.getLatitude()+" and longitude "+dto.getLongitude()+" is already exist";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.CONFLICT, message);
            }
        }
        else
        {
            String message = "input data is empty";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Transactional
    @Override
    public Optional<LocationPointDto> update(LocationPointDto dto) throws ResponseStatusException{
        log.info("update information about location point");
        if (dto!=null)
        {
            Optional<LocationPoint> box = locationPointRepository.findById(dto.getId());
            if (box.isPresent())
            {
                LocationPoint entity = box.get();
                Boolean isValid = !locationPointRepository.findByLatitudeAndLongitude(dto.getLatitude(), dto.getLongitude()).isPresent();
                if (!isValid)
                {
                    String message = "locationPoint with latitude "+dto.getLatitude()+" and longitude "+dto.getLongitude()+" is already exist";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.CONFLICT, message);
                }
                entity = locationPointMapper.toEntity(dto);
                entity = locationPointRepository.save(entity);
                return Optional.ofNullable(locationPointMapper.toDto(entity));
            }
            else
            {
                String message = "locationPoint with id "+dto.getId()+" was not found";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        }
        else {
            String message = "input data is empty";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Transactional
    @Override
    public void remove(Long id) throws ResponseStatusException {
        log.info("removing location point with id {}", id);
        if (id!=null)
        {
            Optional<LocationPoint> box = locationPointRepository.findById(id);
            if (box.isPresent())
            {
                Optional<Animal> animalBox = animalRepository.findByChippingLocationId_Id(id);
                if (!animalBox.isPresent())
                {
                    locationPointRepository.deleteById(id);
                    log.info("locationPoint removing success");
                }
                else
                {
                    String message = "location point with id "+id+" use in animal entity, id "+animalBox.get().getId();
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
            }
            else
            {
                String message = "locationPoint with id "+id+" was not found";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        }
        else
        {
            String message = "input data is invalid";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }
}
