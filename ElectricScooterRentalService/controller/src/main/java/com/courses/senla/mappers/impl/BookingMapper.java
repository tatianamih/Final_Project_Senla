package com.courses.senla.mappers.impl;

import com.courses.senla.dto.BookingCreateDto;
import com.courses.senla.dto.BookingDto;
import com.courses.senla.dto.BookingOpenDto;
import com.courses.senla.dto.PageDto;
import com.courses.senla.mappers.GenericBookingMapper;
import com.courses.senla.models.Booking;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for converting the Booking entity to / from DTO
 */
@Component
public class BookingMapper implements GenericBookingMapper<Booking, BookingDto, BookingCreateDto, BookingOpenDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BookingDto toDto(Booking booking) {
        return Objects.isNull(booking) ? null : modelMapper.map(booking, BookingDto.class);
    }

    @Override
    public BookingOpenDto toDtoOpen(Booking booking) {
        return Objects.isNull(booking) ? null : modelMapper.map(booking, BookingOpenDto.class);
    }

    @Override
    public Booking toEntity(BookingDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Booking.class);
    }

    @Override
    public Booking toEntityMethCreate(BookingCreateDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Booking.class);
    }

    @Override
    public List<BookingDto> toDtoList(List<Booking> entityList) {
        return entityList.stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> toEntityList(List<BookingDto> dtoList) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, Booking.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageDto toDtoPage(Page<Booking> entitiesPage) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entitiesPage.getContent()
                .stream().map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());
        return pageDto;
    }
}
