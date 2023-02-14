package com.example.demo.controller;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.model.LifeStatus;
import com.example.demo.service.*;
import com.example.demo.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/animals")
public class AnimalRestController {


    private final AnimalTypeService animalTypeService;

    private final AnimalService animalService;

    private final AnimalVisitedLocationService animalVisitedLocationService;

    private final AccountService accountService;

    private final LocationPointService locationPointService;

    @Autowired
    public AnimalRestController(AnimalTypeService animalTypeService, AnimalService animalService, AnimalVisitedLocationService animalVisitedLocationService, AccountService accountService, LocationPointService locationPointService) {
        this.animalTypeService = animalTypeService;
        this.animalService = animalService;
        this.animalVisitedLocationService = animalVisitedLocationService;
        this.accountService = accountService;
        this.locationPointService = locationPointService;
    }

    //animals is here

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<AnimalDto> findAnimalById(@PathVariable Long id)
    {
        log.info("searching information about animal by id {}", id);
        if (checkId(id))
        {
            Optional<AnimalDto> box = animalService.findById(id);
            if (box.isPresent())
            {
                log.info("find animal with id {}",id);
                return new ResponseEntity<>(box.get(), HttpStatus.OK);
            }
            else
            {
                log.warn("animal was not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            log.warn("id is mandatory and must be positive");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search")
    public ResponseEntity<List<AnimalDto>> searchAnimal(AnimalSearchDto dto, Pageable pageable)
    {
        log.info("search animal by parameters");
        List<AnimalDto> dtoList = new ArrayList();
        Boolean isValidPagination = pageable.getPageNumber()>=0 && pageable.getPageSize()>0;
        if (!isValidPagination)
        {
            log.warn("pagination parameters must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            dtoList = animalService.search(dto, pageable);
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        }
        catch (ResponseStatusException ex)
        {
            log.warn(ex.getMessage());
            return new ResponseEntity<>(ex.getStatusCode());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<AnimalDto> addNewAnimal(@Valid @RequestBody AnimalDto dto)
    {
        log.info("adding new animal");
        try {
            Optional<AnimalDto> box = animalService.add(dto);
            if (box.isPresent())
            {
                log.info("adding success");
                return new ResponseEntity<>(box.get(), HttpStatus.CREATED);
            }
            else
            {
                log.warn("adding failed");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        catch (ResponseStatusException ex)
        {
            log.warn(ex.getMessage());
            return new ResponseEntity<>(ex.getStatusCode());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<AnimalDto> updateAnimal(@PathVariable Long id, @Valid @RequestBody AnimalDto dto)
    {
        log.info("updating information about animal");
        Boolean isValid = checkId(id);
        if (isValid)
        {
            dto.setId(id);
            try
            {
                Optional<AnimalDto> box = animalService.update(dto);
                if (box.isPresent())
                {
                    log.info("updating success");
                    return new ResponseEntity<>(box.get(), HttpStatus.OK);
                }
                else
                {
                    log.warn("updating failed");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity<>(ex.getStatusCode());
            }
        }
        else
        {
            log.warn("id is mandatory and must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAnimal(@PathVariable Long id)
    {
        log.info("removing animal with id "+id);
        if (checkId(id))
        {
            try
            {
                animalService.remove(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity<>(ex.getStatusCode());
            }
        }
        else
        {
            log.warn("id is mandatory and must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{animalId}/types/{typeId}")
    public ResponseEntity<AnimalDto> addAnimalTypeToAnimal(@PathVariable("animalId") Long animalId, @PathVariable("typeId") Long typeId)
    {
        log.info("adding animal type to animal");
        Boolean isValid = checkId(animalId) && checkId(typeId);
        if (isValid)
        {
           try
           {
               Optional<AnimalDto> box = animalService.addAnimalTypeToAnimal(animalId, typeId);
               if (box.isPresent())
               {
                   log.info("adding success");
                   return new ResponseEntity<>(box.get(), HttpStatus.OK);
               }
               else
               {
                   log.warn("adding failed");
                   return new ResponseEntity<>(HttpStatus.NO_CONTENT);
               }
           }
           catch (ResponseStatusException ex)
           {
               log.warn(ex.getMessage());
               return new ResponseEntity(ex.getStatusCode());
           }
        }
        else
        {
            log.warn("animalId and typeId are mandatory and must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{animalId}/types")
    public ResponseEntity<AnimalDto> updateAnimalTypeForAnimal(@PathVariable("animalId") Long animalId, @Valid @RequestBody AnimalTypeUpdateDto dto)
    {
        log.info("updating animal type for animal");
        Boolean isValid = checkId(animalId) && dto!=null;
        if (isValid)
        {
            try
            {
                Optional<AnimalDto> box = animalService.updateAnimalTypeForAnimal(animalId, dto);
                if (box.isPresent())
                {
                    log.info("updating success");
                    return new ResponseEntity<>(box.get(), HttpStatus.OK);
                }
                else
                {
                    log.warn("updating failed");
                    return new ResponseEntity(HttpStatus.NO_CONTENT);
                }
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity(ex.getStatusCode());
            }
        }
        else
        {
            log.warn("input data is invalid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{animalId}/types/{typeId}")
    public ResponseEntity<Void> removeAnimalTypeForAnimal(@PathVariable("animalId") Long animalId, @Valid @PathVariable("typeId") Long typeId)
    {
        log.info("removing animal type for animal");
        if (checkId(animalId) && checkId(typeId))
        {
            try {
                Optional<AnimalDto> box = animalService.removeAnimalTypeForAnimal(animalId, typeId);
                if (box.isPresent())
                {
                    log.info("removing success");
                    return new ResponseEntity(box.get(),HttpStatus.OK);
                }
                else
                {
                    log.warn("removing failed");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity(ex.getStatusCode());
            }
        }
        else
        {
            log.warn("input data is invalid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //animalVisitedLocations is here

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}/locations")
    public ResponseEntity<List<AnimalVisitedLocationDto>> findAnimalVisitedLocations(@PathVariable Long id, AnimalVisitedLocationSearchDto dto, Pageable pageable)
    {
        log.info("search animal visited locations");
        Boolean isValidRequest = pageable.getPageNumber()>=0 && pageable.getPageSize()>0;
        if (isValidRequest)
        {
            if (checkId(id))
            {
                try {
                   List<AnimalVisitedLocationDto> list = animalVisitedLocationService.findAll(id, dto, pageable);
                   return new ResponseEntity<>(list, HttpStatus.OK);
                }
                catch (ResponseStatusException ex)
                {
                    log.warn(ex.getMessage());
                    return new ResponseEntity(ex.getStatusCode());
                }
            }
            else
            {
                log.warn("id is mandatory and must be positive");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            log.warn("pagination attributes are invalid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{animalId}/locations/{pointId}")
    public ResponseEntity<AnimalVisitedLocationDto> addAnimalVisitedLocationPoint(@PathVariable("animalId") Long animalId, @PathVariable("pointId") Long pointId)
    {
        log.info("adding location point");
        Boolean isValid = checkId(animalId) && checkId(pointId);
        if (isValid)
        {
            try
            {
                Optional<AnimalVisitedLocationDto> box = animalVisitedLocationService.add(animalId, pointId);
                if (box.isPresent())
                {
                    log.info("adding success");
                    return new ResponseEntity<>(box.get(), HttpStatus.CREATED);
                }
                else
                {
                    log.warn("adding failed");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity<>(ex.getStatusCode());
            }
        }
        else
        {
            log.info("animalId and pointId are mandatory and must be positive");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}/locations")
    public ResponseEntity<AnimalVisitedLocationDto> updateAnimalVisitedLocationPoint(@PathVariable Long id, @Valid @RequestBody  AnimalVisitedLocationUpdateDto dto)    {
        log.info("updating information about animal visited location");
        Boolean isValid = checkId(id);
        if (isValid)
        {
            try
            {
                Optional<AnimalVisitedLocationDto> box = animalVisitedLocationService.update(id, dto);
                if (box.isPresent())
                {
                    return new ResponseEntity<>(box.get(), HttpStatus.OK);
                }
                else
                {
                    log.warn("updating failed");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity<>(ex.getStatusCode());
            }
        }
        else
        {
            log.warn("id is mandatory and must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{animalId}/locations/{pointId}")
    public ResponseEntity<Void> removeAnimalVisitedLocationPoint(@PathVariable Long animalId, @PathVariable Long pointId)
    {
        log.info("removing visited location point {} for animal {}", pointId, animalId);
        Boolean isValid = checkId(animalId) && checkId(pointId);
        if (isValid)
        {
            try
            {
                animalVisitedLocationService.remove(animalId, pointId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity<>(ex.getStatusCode());
            }

        }
        else
        {
            log.warn("animal id and animal visited location point id are mandatory and must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //animal types is here

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/types/{id}")
    public ResponseEntity<AnimalTypeDto> findBAnimalTypeById(@PathVariable Long id)
    {
        log.info("searching animal type by id");
        if (checkId(id))
        {
            Optional<AnimalTypeDto> box = animalTypeService.findById(id);
            if (box.isPresent())
            {
                log.info("find animal type with id {}", id);
                return new ResponseEntity<>(box.get(), HttpStatus.OK);
            }
            else
            {
                log.warn("animal type was not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            log.warn("id is mandatory and must be positive");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/types")
    public ResponseEntity<AnimalTypeDto> addAnimalType(@Valid @RequestBody AnimalTypeDto dto)
    {
        log.info("adding new animal type");
        try
        {
            Optional<AnimalTypeDto> box = animalTypeService.add(dto);
            if (box.isPresent())
            {
                log.info("adding success");
                return new ResponseEntity<>(box.get(), HttpStatus.CREATED);
            }
            else
            {
                log.warn("adding failed");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        catch (ResponseStatusException ex)
        {
            log.warn(ex.getMessage());
            return new ResponseEntity<>(ex.getStatusCode());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/types/{id}")
    public ResponseEntity<AnimalTypeDto> updateAnimalType(@PathVariable Long id, @Valid @RequestBody AnimalTypeDto dto)
    {
        log.info("updating information about animal type");
        if (checkId(id))
        {
            dto.setId(id);
            try
            {
                Optional<AnimalTypeDto> box = animalTypeService.update(dto);
                if (box.isPresent())
                {
                    log.info("updating success");
                    return new ResponseEntity<>(box.get(), HttpStatus.OK);
                }
                else
                {
                    log.warn("updating failed");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity<>(ex.getStatusCode());
            }
        }
        else
        {
            log.info("id is mandatory and must be positive");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> removeAnimalType(@PathVariable Long id)
    {
        log.info("removing animal type with id "+id);
        if (checkId(id))
        {
            try {
                animalTypeService.remove(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch (ResponseStatusException ex)
            {
                log.warn(ex.getMessage());
                return new ResponseEntity<>(ex.getStatusCode());
            }
        }
        else
        {
            log.warn("id is mandatory and must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    private Boolean checkId(Long id)
    {
        Boolean isAllowId = false;
        if (id!=null && !id.equals(""))
        {
            isAllowId = id>0;
        }
        return isAllowId;
    }

   /* @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidatorExceptions(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors = new HashMap();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex)
    {
        String message = "MethodArgumentTypeMismatchException was occurred; check enum values";
        log.error(message);
        log.error(ex.getStackTrace().toString());
        return message;
    }*/

}
