package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnimalTypeDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotNull
    private String type;

}
