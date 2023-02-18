package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * dto класс AnimalVisitedLocation для организации обновления
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Data
public class AnimalVisitedLocationUpdateDto {

    @JsonProperty
    @NotNull(message = "visited location point id is mandatory")
    @Positive(message = "visited location point id must be positive")
    private Long visitedLocationPointId;

    @JsonProperty
    @NotNull(message = "location point id is mandatory")
    @Positive(message = "location point is must be positive")
    private Long locationPointId;

}
