package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class AnimalTypeUpdateDto {

    @JsonProperty
    @NotBlank(message = "oldType is mandatory")
    @Positive(message = "oldType must be positive")
    private Long oldType;

    @JsonProperty
    @NotBlank(message = "newType is mandatory")
    @Positive(message = "newType must be positive")
    private Long newType;
}
