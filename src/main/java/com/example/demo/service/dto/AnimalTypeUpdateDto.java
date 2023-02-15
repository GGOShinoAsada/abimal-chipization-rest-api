package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class AnimalTypeUpdateDto {

    @JsonProperty
    @NotNull(message = "oldType is mandatory")
    @Positive(message = "oldType must be positive")
    private Long oldType;

    @JsonProperty
    @NotNull(message = "newType is mandatory")
    @Positive(message = "newType must be positive")
    private Long newType;
}
