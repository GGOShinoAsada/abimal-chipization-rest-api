package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@Data
public class AccountDto {

    @JsonProperty("id")
    @NotNull(message = "id is mandatory")
    @Positive(message = "account id must be positive")
    private Integer id;

    @JsonProperty("firstName")
    @NotNull(message = "firstName is mandatory")
    private String firstName;

    @JsonProperty("lastName")
    @NotBlank(message = "lastName is mandatory")
    private String lastName;

    @JsonProperty("email")
    @NotNull(message = "email is mandatory")
    @Email(message = "email is not valid", regexp = "^(.+)@(\\\\S+)$")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "password is mandatory")
    private String password;

    public AccountDto(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}
