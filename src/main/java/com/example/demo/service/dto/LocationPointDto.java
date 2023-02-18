package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.validation.constraints.*;

/**
 * dto класс LocationPoint
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@NoArgsConstructor
@Data
public class LocationPointDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotNull(message = "latitude field is mandatory")
    @Min(value = -90, message = "latitude: min value is -90")
    @Max(value = 90, message = "latitude: max value is 90")
    private Double latitude;

    @JsonProperty
    @NotNull(message = "longitude field is mandatory")
    @Min(value = -180, message = "longitude: min value is -180")
    @Max(value = 180, message = "longitude: max value is 180")
    private Double longitude;

}
