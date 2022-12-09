package com.courses.senla.dto;


import com.courses.senla.dto.interf.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class ScooterTypeDto implements BaseDto {
    private Long id;

    @NotBlank
    private String model;

    @NotNull
    private Integer maxSpeed;
}

