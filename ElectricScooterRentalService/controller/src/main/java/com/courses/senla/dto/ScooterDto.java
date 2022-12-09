package com.courses.senla.dto;


import com.courses.senla.dto.interf.BaseDto;
import com.courses.senla.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ScooterDto implements BaseDto {
    private Long id;
    @NotBlank
    private String serialNumber;

    @NotNull
    private Integer releaseYear;

    @NotNull
    private Double mileage;

    @NotBlank
    private Status status;

    @NotNull
    private Double coordinateX;

    @NotNull
    private Double coordinateY;

    @NotNull
    private BigDecimal costPerHour;

    @NotNull
    private Long stationId;

    @NotNull
    private Long scooterTypeId;
}
