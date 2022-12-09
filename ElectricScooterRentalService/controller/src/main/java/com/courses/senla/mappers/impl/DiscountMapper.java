package com.courses.senla.mappers.impl;

import com.courses.senla.dto.DiscountCreateDto;
import com.courses.senla.dto.DiscountDto;
import com.courses.senla.dto.PageDto;
import com.courses.senla.mappers.GenericMapper;
import com.courses.senla.models.Discount;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for converting the Discount entity to / from DTO
 */
@Component
public class DiscountMapper implements GenericMapper<Discount, DiscountDto, DiscountCreateDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DiscountDto toDto(Discount discount) {
        return Objects.isNull(discount) ? null : modelMapper.map(discount, DiscountDto.class);
    }

    @Override
    public Discount toEntity(DiscountDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Discount.class);
    }

    @Override
    public Discount toEntityMethCreate(DiscountCreateDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Discount.class);
    }

    @Override
    public List<DiscountDto> toDtoList(List<Discount> entityList) {
        return entityList.stream()
                .map(discount -> modelMapper.map(discount, DiscountDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Discount> toEntityList(List<DiscountDto> dtoList) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, Discount.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageDto toDtoPage(Page<Discount> entitiesPage) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entitiesPage.getContent()
                .stream().map(discount -> modelMapper.map(discount, DiscountDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());
        return pageDto;
    }
}
