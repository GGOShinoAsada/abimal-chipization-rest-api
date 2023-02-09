package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class AnimalTypeDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotBlank(message = "type is mandatory and must be positive")
    private String type;

    public Boolean checkSpaces()
    {
        Boolean check = false;
        if (type!=null)
            check = type.contains(" ");
        return check;
    }

}
