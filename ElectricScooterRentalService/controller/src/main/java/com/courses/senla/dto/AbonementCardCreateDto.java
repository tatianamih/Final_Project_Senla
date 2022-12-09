package com.courses.senla.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AbonementCardCreateDto {
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private LocalDateTime created;

    @JsonIgnore
    private LocalDateTime updated;

    @JsonIgnore
    private boolean deleted;

    @NotBlank
    private String name;

    @JsonIgnore
    private BigDecimal abonementCardCost;

    @NotNull
    private BigDecimal abonementCardBalance;
 }
