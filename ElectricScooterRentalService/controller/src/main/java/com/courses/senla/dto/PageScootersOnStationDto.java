package com.courses.senla.dto;

import com.courses.senla.dto.interf.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class PageScootersOnStationDto {
    private Long number;

    private ArrayList<BaseDto> list;

    private Integer totalPages;
}
