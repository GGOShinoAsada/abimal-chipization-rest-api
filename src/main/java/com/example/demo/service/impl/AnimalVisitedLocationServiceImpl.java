package com.example.demo.service.impl;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalVisitedLocation;
import com.example.demo.model.LifeStatus;
import com.example.demo.model.LocationPoint;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.repository.AnimalVisitedLocationRepository;
import com.example.demo.repository.LocationPointRepository;
import com.example.demo.service.AnimalVisitedLocationService;
import com.example.demo.service.dto.AnimalVisitedLocationDto;
import com.example.demo.service.dto.AnimalVisitedLocationSearchDto;
import com.example.demo.service.dto.AnimalVisitedLocationUpdateDto;
import com.example.demo.service.mapper.AnimalVisitedLocationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class AnimalVisitedLocationServiceImpl implements AnimalVisitedLocationService {



    private final AnimalVisitedLocationRepository animalVisitedLocationRepository;

    private final AnimalVisitedLocationMapper animalVisitedLocationMapper;

    private final AnimalRepository animalRepository;


    private LocationPointRepository locationPointRepository;

    @Autowired
    public AnimalVisitedLocationServiceImpl(AnimalVisitedLocationRepository animalVisitedLocationRepository, AnimalVisitedLocationMapper animalVisitedLocationMapper, AnimalRepository animalRepository, LocationPointRepository locationPointRepository) {
        this.animalVisitedLocationRepository = animalVisitedLocationRepository;
        this.animalVisitedLocationMapper = animalVisitedLocationMapper;
        this.animalRepository = animalRepository;
        this.locationPointRepository = locationPointRepository;
    }

    @Transactional
    @Override
    public List<AnimalVisitedLocationDto> findAll(Long id, AnimalVisitedLocationSearchDto dto, Pageable pageable) throws ResponseStatusException {
        log.info("find all visited location points by parameters");
        List<AnimalVisitedLocationDto> dtoList = new ArrayList();
        if (dto!=null  && id!=null)
        {
            Boolean isValid = false;
            Date startDateTime = null;
            Date endDateTime = null;
            if (dto.getStartDateTime()!=null && dto.getEndDateTime()!=null)
            {
                try
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                    startDateTime = sdf.parse(dto.getStartDateTime());
                    endDateTime = sdf.parse(dto.getEndDateTime());
                    isValid = true;
                }
                catch (ParseException ex)
                {
                    isValid = false;
                }
                if (!isValid)
                {
                    String message = "startDateTime or endDateTime syntax is invalid";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
            }
            Optional<Animal> box = animalRepository.findById(id);
            if (box.isPresent())
            {
                List<AnimalVisitedLocation> list = box.get().getAnimalVisitedLocations();
                if (list!=null)
                {
                    for (AnimalVisitedLocation location: list)
                    {
                        if (dto.getStartDateTime()!=null && dto.getEndDateTime()!=null)
                        {
                            if (location.getDateTimeOfVisitLocationPoint().compareTo(startDateTime)>0
                                    && location.getDateTimeOfVisitLocationPoint().compareTo(endDateTime)<0 )
                            {
                                dtoList.add(animalVisitedLocationMapper.toDto(location));
                            }
                        }
                        else
                        {
                            dtoList.add(animalVisitedLocationMapper.toDto(location));
                        }
                    }
                }
            }
            else
            {
                String message = "animal with id "+id+" was not found";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        }
        else
        {
            String message = "input data is empty";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return dtoList;
    }

    @Transactional
    @Override
    public Optional<AnimalVisitedLocationDto> findById(Long id) {
        log.info("searching animal visited location by id "+id);
        Optional<AnimalVisitedLocation> box= animalVisitedLocationRepository.findById(id);
        if (box.isPresent())
        {
            log.info("found animalVisitedLocation with id "+id);
            return Optional.of(animalVisitedLocationMapper.toDto(box.get()));
        }
        else
        {
            log.warn("animal visited location was not found");
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<AnimalVisitedLocationDto> add(Long animalId, Long pointId) throws ResponseStatusException {
        log.info("adding new animal visited location");
        if (animalId!=null && pointId!=null)
        {
            Optional<Animal> animalBox = animalRepository.findById(animalId);
            Optional<LocationPoint> locationPointBox = locationPointRepository.findById(pointId);
            if (animalBox.isPresent() && locationPointBox.isPresent())
            {
                Animal animal = animalBox.get();
                LocationPoint locationPoint = locationPointBox.get();
                List<AnimalVisitedLocation> list = animal.getAnimalVisitedLocations();
                if (animal.getLifeStatus().equals(LifeStatus.DEAD))
                {
                    String message = "trying to add location point to dead animal";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                //попытка добавить точку локации, равную точке чипирования
                if (animal.getChippingLocationId().getId().equals(pointId))
                {
                    String message = "trying to add a location point equal to the chipping point";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
                }

                //попытка добавить точку локации, в которой уже находится животное
                Boolean isValid = true;
                if (list!=null)
                {
                    if (list.size()>0)
                        isValid = !(list.get(list.size()-1).getId().equals(pointId));
                }
                if (!isValid)
                {
                    String message = "trying to add a location point where the animal is already located";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }

                AnimalVisitedLocation entity = new AnimalVisitedLocation();
                entity.setLocationPoint(locationPoint);
                entity.setDateTimeOfVisitLocationPoint(new Date());
                entity = animalVisitedLocationRepository.save(entity);
                if (entity!=null)
                {
                    list.add(entity);
                    animal.setAnimalVisitedLocations(list);
                    animal = animalRepository.save(animal);
                    if (animal!=null)
                    {
                        log.info("adding success");
                    }
                    else
                    {
                        log.warn("animal save failed");
                        //rollback
                        animalVisitedLocationRepository.delete(entity);
                        entity = null;
                    }
                }
                else
                {
                    log.warn("animalVisited location save failed");
                }

                return Optional.ofNullable(animalVisitedLocationMapper.toDto(entity));
            }
            else
            {
                String message = "animal with id "+animalId+" or locationPoint with id "+pointId+" wqs not found";
                log.warn(message);
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
    public Optional<AnimalVisitedLocationDto> update(Long animalId, AnimalVisitedLocationUpdateDto dto) throws ResponseStatusException {
        log.info("updating information about visited location point");
        if (animalId!=null && dto!=null)
        {

            Optional<Animal> animalBox = animalRepository.findById(animalId);
            Optional<AnimalVisitedLocation> animalVisitedLocationBox = animalVisitedLocationRepository.findById(dto.getVisitedLocationPointId());
            Optional<LocationPoint> locationPointBox = locationPointRepository.findById(dto.getLocationPointId());
            if (animalBox.isPresent() && animalVisitedLocationBox.isPresent() && locationPointBox.isPresent())
            {
                Animal animal = animalBox.get();
                List<AnimalVisitedLocation> list = animal.getAnimalVisitedLocations();
                Boolean isValid = false;
                if (list!=null)
                {
                    for (AnimalVisitedLocation item: list)
                    {
                        if (item.getId().equals(dto.getVisitedLocationPointId()))
                        {
                            isValid = true;
                            break;
                        }
                    }
                }
                if (!isValid)
                {
                    String message = "animal with id"+animalId+ " isn't contains visitedLocationPoint with id "+dto.getVisitedLocationPointId();
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                }

                //обновление первой посещенной точки на точку чипирования
                isValid = true;
                if (list!=null)
                {
                    if (list.size()>0)
                    {
                        AnimalVisitedLocation item = list.get(0);
                        isValid = !(item.getId().equals(dto.getVisitedLocationPointId()) &&
                                animal.getChippingLocationId().getId().equals(dto.getLocationPointId()));
                    }
                }
                if (!isValid)
                {
                    String message = "trying to update the first visited point to the point of chipping";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                //обновление точки на такую же точку
                isValid = true;
                if (list!=null)
                {
                    if (list.size()>0)
                    {
                        AnimalVisitedLocation item = new AnimalVisitedLocation();
                        for (AnimalVisitedLocation location: list)
                        {
                            if (location.getId().equals(dto.getVisitedLocationPointId()))
                            {
                                item = location;
                            }
                        }
                        isValid = !item.getLocationPoint().getId().equals(dto.getLocationPointId());
                    }
                }
                if (!isValid)
                {
                    String message = "trying to update point on the same point";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }
                //обновление точки локации на точку, совпадающую со следующей и/или с предыдущей точками
                isValid = true;
                if (list!=null)
                {
                    for (AnimalVisitedLocation item: list)
                    {
                        isValid = !(item.getLocationPoint().getId().equals(dto.getLocationPointId()));
                        if (!isValid)
                        {
                            break;
                        }
                    }
                }
                if (!isValid)
                {
                    String message = "trying to update the locate point to a point that is the same as the next and/or previous point";
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }

                AnimalVisitedLocation entity = animalVisitedLocationBox.get();
                LocationPoint point = locationPointBox.get();
                entity.setLocationPoint(point);
                entity = animalVisitedLocationRepository.save(entity);
                return Optional.ofNullable(animalVisitedLocationMapper.toDto(entity));
            }
            else
            {
                String message = "animal with id "+animalId+", animalVisitedLocation with id "+dto.getVisitedLocationPointId()+" or locationPoint with id "+dto.getLocationPointId()+" was not found";
                log.warn(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        }
        else
        {
            String message ="input data is empty";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Transactional
    @Override
    public void remove(Long animalId, Long pointId) throws ResponseStatusException{
        log.info("removing animalVisitedLocation with id {} for animal with id {}", pointId, animalId);
        if (animalId!=null && pointId!=null)
        {
            Optional<Animal> animalBox = animalRepository.findById(animalId);
            Optional<AnimalVisitedLocation> animalVisitedLocationBox = animalVisitedLocationRepository.findById(pointId);
            if (animalBox.isPresent() && animalVisitedLocationBox.isPresent())
            {
                Animal animal = animalBox.get();
                List<AnimalVisitedLocation> list = animal.getAnimalVisitedLocations();
                Integer index = -1;
                if (list!=null)
                {
                    for (int i=0; i<list.size(); i++)
                    {
                        if (list.get(i).getId().equals(pointId))
                        {
                            index = i;
                            break;
                        }
                    }
                }
                if (index == -1)
                {
                    String message = "animal with id "+animalId+" isn't contains animalVisitedLocation with id "+pointId;
                    log.warn(message);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                }

                if (list.size()>1)
                {
                    if (index==0 && list.get(1).getLocationPoint().getId().equals(animal.getChippingLocationId().getId()))
                    {
                        animalVisitedLocationRepository.deleteById(list.get(1).getId());
                        list.remove(1);
                    }
                }
                animalVisitedLocationRepository.deleteById(list.get(index).getId());
                list.remove(index);
                animal.setAnimalVisitedLocations(list);
                animal = animalRepository.save(animal);
            }
            else
            {
                String message = "animal with id "+animalId+" or animalVisitedLocation with id "+pointId+" was not found";
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
