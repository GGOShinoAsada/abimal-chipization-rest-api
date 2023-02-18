package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;


/**
 * dto класс AnimalVisitedLocation для организации поиска
 * @author ROMAN
 * @date 20223-02-17
 * @version 1.0
 */
@Data
public class AnimalVisitedLocationSearchDto {

    @JsonProperty
    @NotNull(message = "startDateTime is mandatory")
    private String startDateTime;

    @JsonProperty
    @NotNull(message = "endDateTime is mandatory")
    private String endDateTime;
}
