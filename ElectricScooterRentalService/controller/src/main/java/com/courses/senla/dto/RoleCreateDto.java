package com.courses.senla.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class RoleCreateDto {
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private LocalDateTime created;

    private boolean deleted;

    @NotBlank
    private String name;
}
