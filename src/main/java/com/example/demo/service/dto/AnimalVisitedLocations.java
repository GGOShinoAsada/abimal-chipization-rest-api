package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AnimalVisitedLocations {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotNull
    private Long locationPointId;

    private LocationPointDto locationPoint;

    @JsonProperty
    @NotNull
    private Date dateTimeOfVisitedLocationPoint;

}
