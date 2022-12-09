package com.courses.senla.mappers.impl;

import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.UserAccountHistoryDto;
import com.courses.senla.mappers.GenericHistoryMapper;
import com.courses.senla.models.UserAccountHistory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;


@Component
public class UserAccountHistoryMapper implements GenericHistoryMapper<UserAccountHistory> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PageDto toDtoPage(Page<UserAccountHistory> entityPages) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entityPages.getContent()
                .stream().map(userAccountHistory -> modelMapper.map(userAccountHistory, UserAccountHistoryDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entityPages.getTotalPages());

        return pageDto;
    }
}
