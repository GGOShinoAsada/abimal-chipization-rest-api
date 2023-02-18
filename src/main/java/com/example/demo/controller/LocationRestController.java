package com.example.demo.controller;

import com.example.demo.config.ResponseStatusException;
import com.example.demo.service.LocationPointService;
import com.example.demo.service.dto.LocationPointDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

/**
 * контроллер для точек локации животных
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/locations")
public class LocationRestController {


    private final LocationPointService locationPointService;

    @Autowired
    public LocationRestController(LocationPointService locationPointService) {
        this.locationPointService = locationPointService;
    }

    /**
     * получение информации о точке локации животных
     * запрос может быть выполнен только пользователями с ролью user
     * @param id
     * @param session
     * @return
     * 200 - запрос успешно выполнен;
     * 400 - неверные апараметры запроса;
     * 401 - неверные авторизационные данные;
     * 404 - точка локации с id не найдена;
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<LocationPointDto> findByLocationPointId(@PathVariable Long id, HttpSession session)
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

    /**
     * добавление точки локации животных
     * запрос может выполнять только пользователь с ролью user
     * @param dto
     * @param request
     * @return
     * 201 - запрос успешно выполнен;
     * 400 - неверные пармметры запроса;
     * 401 - запрос от неваторизованного акаунта, неверные авторизационные данные;
     * 409 точка локации с latitude и longitude уже существует;
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<LocationPointDto> addLocationPoint(@Valid @RequestBody LocationPointDto dto, HttpServletRequest request)
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

    /**
     * обновление точки локации животных
     * запрос может выполнять только пользователь с ролью user
     * @param id
     * @param dto
     * @param request
     * @return
     * 200 - запрос успешно выполнен;
     * 400 - неверные параметры запроса;
     * 401 - запрос от неавторизованного аккаунта, неверные авторизационные данные;
     * 409 - точка локации с latitude и longitude уже существует;
     * 404 - точка локации с id не найдена;
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<LocationPointDto> updateLocationPoint(@PathVariable Long id, @Valid @RequestBody LocationPointDto dto, HttpServletRequest request)
    {
        String username = (String) request.getSession().getAttribute("user");
        log.info("username "+username);
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

    /**
     * удаление точки локаци животных
     * запрос может выполнять только пользователь с ролью user
     * @param id
     * @param request
     * @return
     * 200 - запрос успешно выполнен;
     * 404 - точка локации с id не найдена;
     * 401 - запрос от неавторизованного аккаунта, неверные авторизационые данные;
     * 400 - неверные парметриы запроса;
     */

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLocationPoint(@PathVariable Long id, HttpServletRequest request)
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

    /**
     * валидация id
     * @param id
     * @return isValid value
     */
    private final Boolean checkId(Long id)
    {
        Boolean isAllowId = false;
        if (id!=null)
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
