package com.example.demo.controller;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.LocationPointService;
import com.example.demo.service.dto.LocationPointDto;
import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/locations")
@Validated
public class LocationRestController {


    private final LocationPointService locationPointService;

    @Autowired
    public LocationRestController(LocationPointService locationPointService) {
        this.locationPointService = locationPointService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<LocationPointDto> findByLocationPointId(@PathVariable Long id)
    {
        log.info("searching location point by id {}",id);
        if (checkId(id))
        {
           Optional<LocationPointDto> box = locationPointService.findById(id);
           if (box.isPresent())
           {
               log.info("find location point with id "+box.get().getId());
               return new ResponseEntity(box.get(), HttpStatus.OK);
           }
           else
           {
               log.warn("location point was not found");
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
        }
        else
        {
            log.warn("id is mandatory and must be positive");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<LocationPointDto> addLocationPoint(@RequestBody LocationPointDto dto)
    {
        log.info("adding new location point");
        try
        {
           Optional<LocationPointDto> box = locationPointService.add(dto);
           if (box.isPresent())
           {
               log.info("adding locationPoint success");
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
    public ResponseEntity<LocationPointDto> updateLocationPoint(@PathVariable Long id, @RequestBody LocationPointDto dto)
    {
        log.info("updating information about location point");
        if (checkId(id))
        {
            try
            {
                dto.setId(id);
                Optional<LocationPointDto> box = locationPointService.update(dto);
                if (box.isPresent())
                {
                    log.info("updating successfully");
                    return new ResponseEntity(box.get(), HttpStatus.OK);
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
                return new ResponseEntity<>(ex.getStatusCode());
            }
        }
        else
        {
            log.warn("id is mandatory and must be positive");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLocationPoint(@PathVariable Long id)
    {
        log.info("removing location point with id "+id);
        if (checkId(id))
        {
            try
            {
                locationPointService.remove(id);
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

    private final Boolean checkId(Long id)
    {
        Boolean isAllowId = false;
        if (id!=null && !id.equals(""))
        {
            isAllowId = id>0;
        }
        return isAllowId;
    }

    /*@ResponseStatus(HttpStatus.BAD_REQUEST)
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
    }*/

}
