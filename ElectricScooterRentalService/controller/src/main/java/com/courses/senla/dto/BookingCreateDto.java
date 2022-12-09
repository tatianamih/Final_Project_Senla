package com.courses.senla.dto;

import com.courses.senla.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingCreateDto {
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Boolean deleted;

    @JsonIgnore
    private LocalDateTime startRentDate;

    @JsonIgnore
    private LocalDateTime endRentDate;

    @JsonIgnore
    private Double distance;

    @JsonIgnore
    private BigDecimal cost;

    @JsonIgnore
    private BookingStatus status;

    private Long userId;

    private Long scooterId;
}



