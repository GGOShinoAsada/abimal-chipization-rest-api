package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@Data
public class LocationPointDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotEmpty(message = "latitude parameter must be positive")
    @Min(value = -90, message = "latitude: min value is -90")
    @Max(value = 90, message = "latitude: max value is 90")
    private Double latitude;

    @JsonProperty
    @NotEmpty(message = "longitude must be positive")
    @Min(value = -180, message = "longitude: min value is -180")
    @Max(value = 180, message = "longitude: max value is 180")
    private Double longitude;

}
