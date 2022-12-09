package com.courses.senla.mappers.impl;

import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.ScooterCreateDto;
import com.courses.senla.dto.ScooterDto;
import com.courses.senla.mappers.GenericMapper;
import com.courses.senla.models.Scooter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for converting the Scooter entity to / from DTO
 */
@Component
public class ScooterMapper implements GenericMapper<Scooter, ScooterDto, ScooterCreateDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ScooterDto toDto(Scooter scooter) {
        return Objects.isNull(scooter) ? null : modelMapper.map(scooter, ScooterDto.class);
    }

    @Override
    public Scooter toEntity(ScooterDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Scooter.class);
    }

    @Override
    public Scooter toEntityMethCreate(ScooterCreateDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Scooter.class);
    }

    @Override
    public List<ScooterDto> toDtoList(List<Scooter> entityList) {
        return entityList.stream()
                .map(scooter -> modelMapper.map(scooter, ScooterDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Scooter> toEntityList(List<ScooterDto> dtoList) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, Scooter.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageDto toDtoPage(Page<Scooter> entitiesPage) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entitiesPage.getContent()
                .stream().map(scooter -> modelMapper.map(scooter, ScooterDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());

        return pageDto;
    }
}
