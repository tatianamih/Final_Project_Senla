package com.courses.senla.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class StationCreateDto {
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private LocalDateTime created;

    private boolean deleted;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotNull
    private Long cityId;
 }
