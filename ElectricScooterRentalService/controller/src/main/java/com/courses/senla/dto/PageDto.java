package com.courses.senla.dto;

import com.courses.senla.dto.interf.BaseDto;
import lombok.Data;

import java.util.ArrayList;

@Data
public class PageDto {

    private ArrayList<BaseDto> list;

    private Integer totalPages;
}
