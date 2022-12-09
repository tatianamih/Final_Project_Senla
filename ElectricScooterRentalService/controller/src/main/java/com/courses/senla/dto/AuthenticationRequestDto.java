package com.courses.senla.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequestDto {
    private String login;
    private String password;
}
