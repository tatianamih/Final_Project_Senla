package com.courses.senla.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class ScooterTypeCreateDto {
    @NotBlank
    private String model;

    @NotNull
    private Integer maxSpeed;
}

