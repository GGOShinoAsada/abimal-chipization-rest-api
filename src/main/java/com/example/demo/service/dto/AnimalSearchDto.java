package com.example.demo.service.dto;

import com.example.demo.model.Gender;
import com.example.demo.model.LifeStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Data
public class AnimalSearchDto {

    @JsonProperty
    @NotNull(message = "startDateTime is mandatory")
    private String startDateTime;

    @JsonProperty
    @NotNull(message = "endDateTime is mandatory")
    private String endDateTime;

    @JsonProperty
    @NotNull(message = "chipperId is mandatory")
    @Positive(message = "chipperId must be positive")
    private Integer chipperId;

    @JsonProperty
    @NotNull(message = "chippingLocationId is mandatory")
    @Positive(message = "chippingLocationId must be positive")
    private Long chippingLocationId;

    @JsonProperty
    @NotNull(message = "lifeStatus is mandatory")
    private LifeStatus lifeStatus;

    @JsonProperty
    @NotNull(message = "gender is mandatory")
    private Gender gender;
}
