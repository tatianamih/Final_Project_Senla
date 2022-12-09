package com.courses.senla.dto;


import com.courses.senla.dto.interf.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class CityDto implements BaseDto {
    private Long id;

    @NotBlank
    private String name;
}

