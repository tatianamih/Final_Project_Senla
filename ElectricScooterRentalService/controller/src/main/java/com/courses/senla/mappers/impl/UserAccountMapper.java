package com.courses.senla.mappers.impl;

import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.UserAccountCreateDto;
import com.courses.senla.dto.UserAccountDto;
import com.courses.senla.mappers.GenericMapper;
import com.courses.senla.models.UserAccount;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for converting the UserAccount entity to / from DTO
 */
@Component
public class UserAccountMapper implements GenericMapper<UserAccount, UserAccountDto, UserAccountCreateDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserAccountDto toDto(UserAccount userAccount) {
        return Objects.isNull(userAccount) ? null : modelMapper.map(userAccount, UserAccountDto.class);
    }

    @Override
    public UserAccount toEntity(UserAccountDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, UserAccount.class);
    }

    @Override
    public UserAccount toEntityMethCreate(UserAccountCreateDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, UserAccount.class);
    }

    @Override
    public List<UserAccountDto> toDtoList(List<UserAccount> entityList) {
        return entityList.stream()
                .map(userAccount -> modelMapper.map(userAccount, UserAccountDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAccount> toEntityList(List<UserAccountDto> dtoList) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, UserAccount.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageDto toDtoPage(Page<UserAccount> entitiesPage) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entitiesPage.getContent()
                .stream().map(userAccount -> modelMapper.map(userAccount, UserAccountDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());
        return pageDto;
    }
 }
