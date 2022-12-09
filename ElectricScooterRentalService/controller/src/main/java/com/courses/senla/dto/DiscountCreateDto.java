package com.courses.senla.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DiscountCreateDto {

    private Boolean deleted;

    @NotBlank
    private String name;

    @Min(0)
    @Max(10)
    private Double percent;

    private Long userId;
}
