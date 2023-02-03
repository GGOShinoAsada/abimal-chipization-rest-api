package com.example.demo.service.dto;

import com.example.demo.model.Gender;
import com.example.demo.model.LifeStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
public class AnimalDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotNull
    private Set<Long> animalTypesIds;

    private Set<AnimalTypeDto> animalTypes;

    @JsonProperty
    @NotNull
    private Float weight;

    @JsonProperty
    @NotNull
    private Float height;

    @JsonProperty
    @NotNull
    private Float length;

    @JsonProperty
    @NotNull
    private Gender gender;

    @JsonProperty
    @NotNull
    private LifeStatus lifeStatus;

    @JsonProperty
    private Date chippingDatetime;

    @JsonProperty
    @NotNull
    private Integer chippedId;

    @JsonProperty(value = "chippingLocationId")
    @NotNull
    private Long localPointId;

    private LocationPointDto chippingLocationId;

    @JsonProperty("visitedLocations")
    private Set<Long> visitedLocationsIds;


    private Set<LocationPointDto> visitedLocationsData;

    @JsonProperty
    private Date deathDatetime;

}
