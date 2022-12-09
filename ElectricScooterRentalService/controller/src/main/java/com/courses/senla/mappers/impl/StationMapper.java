package com.courses.senla.mappers.impl;

import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.StationCreateDto;
import com.courses.senla.dto.StationDto;
import com.courses.senla.mappers.GenericMapper;
import com.courses.senla.models.Station;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for converting the Station entity to / from DTO
 */
@Component
public class StationMapper implements GenericMapper<Station, StationDto,StationCreateDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StationDto toDto(Station station) {
        return Objects.isNull(station) ? null : modelMapper.map(station, StationDto.class);
    }

    @Override
    public Station toEntity(StationDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Station.class);
    }

    @Override
    public Station toEntityMethCreate(StationCreateDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Station.class);
    }

    @Override
    public List<StationDto> toDtoList(List<Station> entityList) {
        return entityList.stream()
                .map(station -> modelMapper.map(station, StationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Station> toEntityList(List<StationDto> dtoList) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, Station.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageDto toDtoPage(Page<Station> entitiesPage) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entitiesPage.getContent()
                .stream().map(station -> modelMapper.map(station, StationDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());
        return pageDto;
    }
}
