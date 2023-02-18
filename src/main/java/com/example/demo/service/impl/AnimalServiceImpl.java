package com.example.demo.service.impl;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.AnimalService;
import com.example.demo.service.dto.*;
import com.example.demo.service.mapper.AnimalMapper;
import com.example.demo.service.mapper.AnimalTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * реализация бизнес логики интерфейса AnimalServcice
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Slf4j
@Service
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;

    private final AnimalTypeRepository animalTypeRepository;

    private final AnimalMapper animalMapper;

    private final AnimalTypeMapper animalTypeMapper;

    private final AnimalVisitedLocationRepository animalVisitedLocationRepository;

    private final AccountRepository accountRepository;

    private final LocationPointRepository locationPointRepository;

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository, AnimalTypeRepository animalTypeRepository, AnimalMapper animalMapper, AnimalTypeMapper animalTypeMapper, AnimalVisitedLocationRepository animalVisitedLocationRepository, AccountRepository accountRepository, LocationPointRepository locationPointRepository) {
        this.animalRepository = animalRepository;
        this.animalTypeRepository = animalTypeRepository;
        this.animalMapper = animalMapper;
        this.animalTypeMapper = animalTypeMapper;
        this.animalVisitedLocationRepository = animalVisitedLocationRepository;
        this.accountRepository = accountRepository;
        this.locationPointRepository = locationPointRepository;
    }

    @Transactional
    @Override
    public Optional<AnimalDto> findById(Long id) {
        log.info("search animal by id {}",id);
        Optional<Animal> box = animalRepository.findById(id);
        if (box.isPresent())
        {
            return Optional.of(animalMapper.toDto(box.get()));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public List<AnimalDto> search(AnimalSearchDto dto, Pageable pageable) throws ResponseStatusException{
        log.info("search animal by parameters");
        Date startDateTime = null;
        Date endDateTime = null;
        Page<Animal> entities = Page.empty();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending());
        if (dto.getStartDateTime() != null && dto.getEndDateTime()!=null && dto.getChipperId()!=null && dto.getChippingLocationId()!=null && dto.getLifeStatus()!=null && dto.getGender()!=null)
        {
            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                startDateTime = sdf.parse(dto.getStartDateTime());
                endDateTime = sdf.parse(dto.getEndDateTime());

            }
            catch (ParseException ex)
            {
                String message = "startDateTime or endDateTime format is invalid";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
            }
            entities =  entities = animalRepository.findByChippingDateTimeAfterAndChippingDateTimeBeforeAndChipperIdAndChippingLocationId_IdAndLifeStatusAndGender(startDateTime, endDateTime, dto.getChipperId(), dto.getChippingLocationId(), dto.getLifeStatus(), dto.getGender(), pageable);
        }
        else
        {
            entities = animalRepository.findAll(pageable);
        }
        List<AnimalDto> dtoList = new ArrayList();
        if (entities.getSize()>0)
        {
            for (Animal entity: entities.toList())
            {
                if (dto.getChippingLocationId()!=null )
                {
                    if (entity.getChippingLocationId().getId().equals(dto.getChippingLocationId()))
                        dtoList.add(animalMapper.toDto(entity));
                }
                else
                {
                    dtoList.add(animalMapper.toDto(entity));
                }
            }
        }
        return dtoList;
    }

    @Transactional
    @Override
    public Optional<AnimalDto> add(AnimalDto dto) throws ResponseStatusException{
        log.info("adding new animal");
        try
        {
            validateAnimal(dto, true);
            dto.setChippingDateTime(new Date());
            dto.setLifeStatus(LifeStatus.ALIVE);

            dto = animalMapper.toDto(animalRepository.save(animalMapper.toEntity(dto)));
            return Optional.ofNullable(dto);
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
    }

    @Transactional
    @Override
    public Optional<AnimalDto> update(AnimalDto dto) throws ResponseStatusException {
        log.info("updating information about animal");
        try
        {
            validateAnimal(dto, false);
            Optional<Animal> box = animalRepository.findById(dto.getId());
            if (box.isPresent())
            {
                Animal entity = box.get();
                Boolean isValid = !(entity.getLifeStatus().equals(LifeStatus.DEAD)
                        && dto.getLifeStatus().equals(LifeStatus.ALIVE));
                if (!isValid)
                {
                    String message = "trying to change lifeStatus for animal with id "+entity.getId()+" from 'DEAD' to 'ALIVE'";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                isValid = true;
                List<AnimalVisitedLocation> list = entity.getAnimalVisitedLocations();
                if (list!=null)
                {
                    for (AnimalVisitedLocation location: list)
                    {
                        isValid = location.getLocationPoint().getId() != entity.getChippingLocationId().getId();
                        if (!isValid)
                            break;
                    }
                }
                if (!isValid)
                {
                    String message = "one of animalVisitedLocationPoints are equal to chippingLocationId for animal with id "+entity.getId();
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                Set<AnimalType> types = entity.getAnimalTypes();
                if (types!=null)
                {
                    Set<Long> typesIds = new HashSet();
                    for (AnimalType item: types)
                    {
                        typesIds.add(item.getId());
                    }
                    dto.setAnimalTypes(typesIds);
                }
                dto.setChippingDateTime(entity.getChippingDateTime());
                entity = animalMapper.toEntity(dto);
                entity = animalRepository.save(entity);
                return Optional.ofNullable(animalMapper.toDto(entity));
            }
            else
            {
                log.warn("animal with id "+dto.getId()+" was not found");
                return Optional.empty();
            }
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
    }

    @Transactional
    @Override
    public void remove(Long id) throws ResponseStatusException {
        log.info("removing animal with id {}",id);
        if (id!=null)
        {
            Optional<Animal> box = animalRepository.findById(id);
            if (box.isPresent())
            {
                Animal entity = box.get();
                Boolean isValid = entity.getAnimalVisitedLocations()!=null;
                if (isValid)
                {
                    isValid = entity.getAnimalVisitedLocations().size()<=1;
                }
                if (!isValid && entity.getAnimalVisitedLocations()!=null)
                {
                    String message = "animal with id "+id+" have several visitedLocationPoints";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                animalRepository.deleteById(id);
            }
            else
            {
                String message = "animal with id "+id+" was not found";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        }
    }

    @Transactional
    @Override
    public Optional<AnimalDto> addAnimalTypeToAnimal(Long animalId, Long typeId) throws ResponseStatusException {
        log.info("adding animalType for animal");
        if (animalId!=null && typeId!=null)
        {
            Optional<Animal> animalBox = animalRepository.findById(animalId);
            Optional<AnimalType> typeBox = animalTypeRepository.findById(typeId);
            if (animalBox.isPresent() && typeBox.isPresent())
            {
                Animal entity = animalBox.get();
                Set<AnimalType> types = entity.getAnimalTypes();
                types.add(typeBox.get());
                entity.setAnimalTypes(types);
                entity = animalRepository.save(entity);
                if (entity!=null)
                {
                    log.info("adding type to animal success");
                    return Optional.of(animalMapper.toDto(entity));
                }
                else
                {
                    log.warn("adding type to animal failed");
                    return Optional.empty();
                }
            }
            else
            {
                String message = "animal with id "+animalId+" or animalType with id"+typeId+" was not found";
                log.warn(message, animalId, typeId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
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
    public Optional<AnimalDto> updateAnimalTypeForAnimal(Long animalId, AnimalTypeUpdateDto typeDto) throws ResponseStatusException {
        log.info("update animalType for animal");
        if (animalId!=null && typeDto!=null)
        {
            Optional<Animal> box = animalRepository.findById(animalId);
            Optional<AnimalType> oldTypeBox = animalTypeRepository.findById(typeDto.getOldType());
            Optional<AnimalType> newTypeBox = animalTypeRepository.findById(typeDto.getNewType());
            if (box.isPresent() && oldTypeBox.isPresent() && newTypeBox.isPresent())
            {
                Optional<AnimalDto> resultBox = Optional.empty();
                Animal entity = box.get();

                List<AnimalType> types = new ArrayList<AnimalType>(entity.getAnimalTypes());
                Boolean isCorrect = false;
                if (types!=null)
                {
                    for (AnimalType type: types)
                    {
                        if (type.getId().equals(typeDto.getOldType()))
                        {
                            isCorrect = true;
                            break;
                        }
                    }
                    if (!isCorrect)
                    {
                        String message ="animalType with id "+typeDto.getOldType()+"was not found for animal with id "+animalId;
                        log.warn(message);
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                    }
                    if (isCorrect)
                    {
                        for (AnimalType type: types)
                        {
                            if (type.getId().equals(typeDto.getNewType()))
                            {
                                isCorrect = false;
                                break;
                            }
                        }
                    }
                    if (isCorrect)
                    {
                        try
                        {
                            Long typeId = oldTypeBox.get().getId();

                            Integer index = -1;
                            for (int i=0; i<types.size(); i++)
                            {
                                if (types.get(i).getId().equals(typeId))
                                {
                                    index = i;
                                    break;
                                }
                            }
                            if (types.get(index)!=null)
                            {
                                types.remove(index.intValue());
                            }
                            types.add(newTypeBox.get());
                            entity.setAnimalTypes(new HashSet<>(types));
                            entity = animalRepository.save(entity);
                            if (entity!=null)
                            {
                                log.info("changing type for animal success");
                                resultBox = Optional.of(animalMapper.toDto(entity));
                            }
                            else
                            {
                                log.warn("changing type for animal failed");
                            }

                        }
                        catch (IndexOutOfBoundsException ex)
                        {
                            log.warn("type with id {} for animal with id {} was not found", typeDto.getOldType(), animalId);
                        }
                    }
                    else
                    {
                        String message = "animal with id "+animalId+" already have type with id "+typeDto.getNewType();
                        log.warn(message);
                        throw new ResponseStatusException(HttpStatus.CONFLICT, message);
                    }
                }

                return resultBox;
            }
            else
            {
                String message = "animal with id "+animalId+" or animalType with id "+typeDto.getOldType()+" or "+typeDto.getNewType()+" was not found";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }

        }
        else
        {
            log.warn("input data is empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "input data is empty");
        }
    }

    @Transactional
    @Override
    public Optional<AnimalDto> removeAnimalTypeForAnimal(Long animalId, Long typeId) throws ResponseStatusException {
        log.info("removing animalType with id {} for animal {}", animalId, typeId);
        if (animalId!=null && typeId!=null)
        {
            Optional<Animal> box = animalRepository.findById(animalId);
            Optional<AnimalType> animalTypeBox = animalTypeRepository.findById(typeId);
            if (box.isPresent() && animalTypeBox.isPresent())
            {
                Animal entity = box.get();
                List<AnimalType> types = new ArrayList<AnimalType>(entity.getAnimalTypes());
                AnimalType removingType = null;
                for (AnimalType type: types)
                {
                    if (type.getId().equals(typeId))
                    {
                        removingType = type;
                    }
                }
                if (removingType!=null)
                {
                    types.remove(removingType);
                    log.info("removing animalType with id {} for animal with id {} success", typeId,animalId);
                    entity.setAnimalTypes(new HashSet(types));
                    entity = animalRepository.save(entity);
                    return Optional.ofNullable(animalMapper.toDto(entity));
                }
                else
                {
                    String message = "animal with id "+animalId+" isn't contains animalType with id "+typeId;
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                }
            }
            else
            {
                String message = "animal with id "+animalId+" or animalType with id "+typeId+" was not found";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        }
        else
        {
            String message = "input data is empty";
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private void validateAnimal(AnimalDto dto, Boolean isCheckingTypes) throws ResponseStatusException
    {
        Boolean isValid = false;
        if (dto!=null)
        {
            if (isCheckingTypes)
            {
                isValid = dto.getAnimalTypes()!=null;
                if (!isValid)
                {
                    String message = "animal must be contains one or more types";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
            }
            else
            {
                isValid = true;
            }
            if (isValid)
            {
                if (isCheckingTypes)
                {
                    for (Long id : dto.getAnimalTypes())
                    {

                        isValid = id!=null;
                        if (isValid)
                            isValid = id>0;
                        if (!isValid)
                        {
                            String message = "animalType is mandatory and must be positive";
                            log.warn(message);
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                        }
                        isValid = animalTypeRepository.findById(id).isPresent();
                        if (!isValid)
                        {
                            String message = "animalType with id "+id+" was not found";
                            log.warn(message);
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                        }
                    }
                }

                isValid = dto.getChipperId()!=null;
                if (isValid)
                {
                    isValid = dto.getChipperId()>0;
                }
                if (!isValid)
                {
                    String message = "chipperId is mandatory and must be positive";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                isValid = accountRepository.findById(dto.getChipperId()).isPresent();
                if (!isValid)
                {
                    String message = "chipperId "+dto.getChipperId()+" was not found";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                }
                isValid = dto.getChippingLocationId()!=null;
                if (isValid)
                {
                    isValid = dto.getChippingLocationId()>0;
                }
                if (!isValid)
                {
                    String message = "chippingLocationId with id "+dto.getChippingLocationId()+" is mandatory and must be positive";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                isValid = locationPointRepository.findById(dto.getChippingLocationId()).isPresent();
                if (!isValid)
                {
                    String message = "locationPoint with id "+dto.getChippingLocationId()+" was not found";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                }
            }

        }
        else
        {
            String message = "input data is empty";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

}
