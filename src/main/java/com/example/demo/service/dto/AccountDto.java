package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class AccountDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("firstName")
    @NotNull
    private String firstName;

    @JsonProperty("lastName")
    @NotNull
    private String lastName;

    @JsonProperty("email")
    @Email
    @NotNull
    private String email;

    @JsonProperty("password")
    @NotNull
    private String password;

}
