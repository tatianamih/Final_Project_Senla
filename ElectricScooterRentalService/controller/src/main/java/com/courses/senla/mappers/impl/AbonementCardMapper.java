package com.courses.senla.mappers.impl;

import com.courses.senla.dto.AbonementCardCreateDto;
import com.courses.senla.dto.AbonementCardDto;
import com.courses.senla.dto.AbonementCardUpdateDto;
import com.courses.senla.dto.PageDto;
import com.courses.senla.mappers.GenericAbonementCardMapper;
import com.courses.senla.mappers.GenericMapper;
import com.courses.senla.models.AbonementCard;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for converting the AbonementCard entity to / from DTO
 */
@Component
public class AbonementCardMapper implements GenericMapper<AbonementCard, AbonementCardDto, AbonementCardCreateDto>,
        GenericAbonementCardMapper<AbonementCard, AbonementCardUpdateDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AbonementCardDto toDto(AbonementCard abonementCard) {
        return Objects.isNull(abonementCard) ? null : modelMapper.map(abonementCard, AbonementCardDto.class);
    }

    @Override
    public AbonementCard toEntity(AbonementCardDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, AbonementCard.class);
    }

    @Override
    public AbonementCard toEntityMethCreate(AbonementCardCreateDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, AbonementCard.class);
    }

    @Override
    public List<AbonementCardDto> toDtoList(List<AbonementCard> entityList) {
        return entityList.stream()
                .map(city -> modelMapper.map(city, AbonementCardDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AbonementCard> toEntityList(List<AbonementCardDto> dtoList) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, AbonementCard.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageDto toDtoPage(Page<AbonementCard> entitiesPage) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entitiesPage.getContent()
                .stream().map(abonementCard -> modelMapper.map(abonementCard, AbonementCardDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());
        return pageDto;
    }

    @Override
    public AbonementCard toEntityMethUpdate(AbonementCardUpdateDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, AbonementCard.class);
    }
}
