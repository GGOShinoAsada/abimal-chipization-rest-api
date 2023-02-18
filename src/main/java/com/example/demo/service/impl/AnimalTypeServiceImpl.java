package com.example.demo.service.impl;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalType;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.repository.AnimalTypeRepository;
import com.example.demo.service.AnimalTypeService;
import com.example.demo.service.dto.AnimalTypeDto;
import com.example.demo.service.mapper.AnimalTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * реализация бизнес логики интерфейса AnimalTypeService
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Slf4j
@Service
public class AnimalTypeServiceImpl implements AnimalTypeService {

    private final AnimalTypeRepository animalTypeRepository;

    private final AnimalRepository animalRepository;

    private final AnimalTypeMapper animalTypeMapper;

    @Autowired
    public AnimalTypeServiceImpl(AnimalTypeRepository animalTypeRepository, AnimalRepository animalRepository, AnimalTypeMapper animalTypeMapper) {
        this.animalTypeRepository = animalTypeRepository;
        this.animalRepository = animalRepository;
        this.animalTypeMapper = animalTypeMapper;
    }

    @Transactional
    @Override
    public Optional<AnimalTypeDto> findById(Long id) {
        log.info("searching animal type by id "+id);
        Optional<AnimalType> box = animalTypeRepository.findById(id);
        if (box.isPresent())
        {
            log.info("fond animalType entity with id "+id);
            return Optional.of(animalTypeMapper.toDto(box.get()));
        }
        else
        {
            log.warn("animal type entity was not found");
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<AnimalTypeDto> add(AnimalTypeDto dto) throws ResponseStatusException {
        log.info("add new animal type");
        if (dto!=null)
        {
            Boolean isValid = validate(dto.getType());
            if (!isValid)
            {
                String message = "type is mandatory and can't contains spaces";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
            }
            isValid = !animalTypeRepository.findByType(dto.getType()).isPresent();
            if (!isValid)
            {
                String message = "type "+dto.getType()+" is already exist";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.CONFLICT, message);
            }
            AnimalType entity = animalTypeRepository.save(animalTypeMapper.toEntity(dto));
            return Optional.ofNullable(animalTypeMapper.toDto(entity));
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
    public Optional<AnimalTypeDto> update(AnimalTypeDto dto) throws ResponseStatusException{
        log.info("update information about animal type");
        if (dto!=null)
        {
            Optional<AnimalType> box = animalTypeRepository.findById(dto.getId());
            if (box.isPresent())
            {
                Boolean isValid = validate(dto.getType());
                if (!isValid)
                {
                    String message = "type is mandatory and can't contains spaces";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                isValid = !animalTypeRepository.findByType(dto.getType()).isPresent();
                if (!isValid)
                {
                    String message = "animalType with type "+dto.getType()+" is already exist";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.CONFLICT, message);
                }
                AnimalType entity = box.get();
                entity = animalTypeRepository.save(animalTypeMapper.toEntity(dto));
                return Optional.ofNullable(animalTypeMapper.toDto(entity));
            }
            else {
                String message = "animalType with id "+dto.getId()+" was not found";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        }
        else
        {
            String message = "";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Transactional
    @Override
    public void remove(Long id) throws ResponseStatusException {
        log.info("removing animal type with id "+id);
        if (id!=null)
        {
            Optional<AnimalType> box = animalTypeRepository.findById(id);
            if (box.isPresent())
            {
                Animal animal = null;
                List<Animal> animals = animalRepository.findAll(Pageable.unpaged()).toList();
                if (animals!=null)
                {
                    for (Animal entity : animals)
                    {
                        List<AnimalType> animalTypes = new ArrayList<AnimalType>(entity.getAnimalTypes());
                        for (AnimalType type: animalTypes)
                        {
                            if (type.getId().equals(id))
                            {
                                log.info("found id in animal entity with id "+entity.getId());
                                animal = entity;
                                break;
                            }
                        }
                        if (animal!=null)
                        {
                            break;
                        }
                    }
                }
                if (animal!=null)
                {
                    String message = "animalType with id "+id+" is already use in animal entity, id "+animal.getId();
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                animalTypeRepository.deleteById(id);
                log.info("animalType removing success");
            }
            else
            {
                String message = "animalType with id "+id+" was not found";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        }
        else
        {
            String message = "input data in invalid";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private Boolean validate(String type)
    {
        Boolean flag = false;
        if (type!=null)
        {
            flag = !type.isEmpty() && !type.contains(" ") && !type.contains("\n") && !type.contains("\t");
        }
        return flag;
    }
}
