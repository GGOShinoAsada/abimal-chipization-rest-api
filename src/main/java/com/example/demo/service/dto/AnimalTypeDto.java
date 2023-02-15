package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class AnimalTypeDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotEmpty(message = "type is mandatory")
    private String type;

}
