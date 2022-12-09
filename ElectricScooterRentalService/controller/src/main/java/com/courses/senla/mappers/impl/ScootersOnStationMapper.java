package com.courses.senla.mappers.impl;

import com.courses.senla.dto.PageScootersOnStationDto;
import com.courses.senla.dto.ScooterDto;
import com.courses.senla.mappers.PageMapper;
import com.courses.senla.models.Scooter;
import com.courses.senla.services.ScooterService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class for converting the Scooter entity to / from DTO
 */
@Component
public class ScootersOnStationMapper implements PageMapper<PageScootersOnStationDto, Scooter> {

    private final ModelMapper modelMapper;

    private final ScooterService scooterService;

    public ScootersOnStationMapper(ModelMapper modelMapper, ScooterService scooterService) {
        this.modelMapper = modelMapper;
        this.scooterService = scooterService;
    }

    @Override
    public PageScootersOnStationDto toDtoPage(Page<Scooter> entitiesPage, Long id) {
        PageScootersOnStationDto pageDto = new PageScootersOnStationDto();
        pageDto.setNumber(scooterService.getScootersNumberOnStation(id));
        pageDto.setList(entitiesPage.getContent()
                .stream().map(scooter -> modelMapper.map(scooter, ScooterDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());

        return pageDto;
    }
}
