package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Data
public class AnimalVisitedLocationSearchDto {

    @JsonProperty
    @NotNull(message = "startDateTime is mandatory")
    private String startDateTime;

    @JsonProperty
    @NotNull(message = "endDateTime is mandatory")
    private String endDateTime;
}
