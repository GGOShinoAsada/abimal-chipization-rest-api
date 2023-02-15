package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@Data
public class AccountViewDto {
    @JsonProperty
    @NotNull(message = "id is mandatory")
    @Positive(message = "account id must be positive")
    private Integer id;

    @JsonProperty
    @NotEmpty(message = "firstName is mandatory")
    private String firstName;

    @JsonProperty
    @NotEmpty(message = "lastName is mandatory")
    private String lastName;

    @JsonProperty
    @NotEmpty(message = "email is mandatory")
    @Email(message = "email is not valid", regexp = "^(.+)@(\\\\S+)$")
    private String email;

}
