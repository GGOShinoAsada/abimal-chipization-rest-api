package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

/**
 * dto класс AnimalType
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@NoArgsConstructor
@Data
public class AnimalTypeDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotEmpty(message = "type is mandatory")
    private String type;

}
