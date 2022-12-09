package com.courses.senla.mappers.impl;

import com.courses.senla.dto.AbonementCardHistoryDto;
import com.courses.senla.dto.PageDto;
import com.courses.senla.mappers.GenericHistoryMapper;
import com.courses.senla.models.AbonementCardHistory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;


@Component
public class AbonementCardHistoryMapper implements GenericHistoryMapper<AbonementCardHistory> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PageDto toDtoPage(Page<AbonementCardHistory> entitiesPage) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entitiesPage.getContent()
                .stream().map(abonementCardHistory -> modelMapper.map(abonementCardHistory, AbonementCardHistoryDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());
        return pageDto;
    }
}
