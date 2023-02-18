package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;

/**
 * dto класс Account
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@NoArgsConstructor
@Data
public class AccountDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("firstName")
    @NotEmpty(message = "firstName is mandatory")
    private String firstName;

    @JsonProperty("lastName")
    @NotEmpty(message = "lastName is mandatory")
    private String lastName;

    @JsonProperty("email")
    @NotEmpty(message = "email is mandatory")
    @Email(message = "email is not valid", regexp = "^(.+)@(.+)$")
    private String email;

    @JsonProperty("password")
    @NotEmpty(message = "password is mandatory")
    private String password;

    public AccountDto(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}
