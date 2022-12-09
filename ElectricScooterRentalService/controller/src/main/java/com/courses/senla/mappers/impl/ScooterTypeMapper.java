package com.courses.senla.mappers.impl;

import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.ScooterTypeCreateDto;
import com.courses.senla.dto.ScooterTypeDto;
import com.courses.senla.mappers.GenericMapper;
import com.courses.senla.models.ScooterType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for converting the ScooterType entity to / from DTO
 */
@Component
public class ScooterTypeMapper implements GenericMapper<ScooterType, ScooterTypeDto, ScooterTypeCreateDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ScooterTypeDto toDto(ScooterType scooterType) {
        return Objects.isNull(scooterType) ? null : modelMapper.map(scooterType, ScooterTypeDto.class);
    }

    @Override
    public ScooterType toEntity(ScooterTypeDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, ScooterType.class);
    }

    @Override
    public ScooterType toEntityMethCreate(ScooterTypeCreateDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, ScooterType.class);
    }

    @Override
    public List<ScooterTypeDto> toDtoList(List<ScooterType> entityList) {
        return entityList.stream()
                .map(scooterType -> modelMapper.map(scooterType, ScooterTypeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScooterType> toEntityList(List<ScooterTypeDto> dtoList) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, ScooterType.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageDto toDtoPage(Page<ScooterType> entitiesPage) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entitiesPage.getContent()
                .stream().map(scooterType -> modelMapper.map(scooterType, ScooterTypeDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());
        return pageDto;
    }
}
