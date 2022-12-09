package com.courses.senla.dto;


import com.courses.senla.dto.interf.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class UserAccountDto implements BaseDto {
    private Long id;

    @NotNull
    private BigDecimal balance;

    private Long userId;
}
