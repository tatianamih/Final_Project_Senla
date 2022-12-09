package com.courses.senla.mappers.impl;

import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.UserCreatDto;
import com.courses.senla.dto.UserDto;
import com.courses.senla.mappers.GenericMapper;
import com.courses.senla.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for converting the User entity to / from DTO
 */
@Component
public class UserMapper implements GenericMapper<User, UserDto, UserCreatDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto toDto(User user) {
        return Objects.isNull(user) ? null : modelMapper.map(user, UserDto.class);
    }

    @Override
    public User toEntity(UserDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, User.class);
    }

    @Override
    public User toEntityMethCreate(UserCreatDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, User.class);
    }

    @Override
    public List<UserDto> toDtoList(List<User> entityList) {
        return entityList.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> toEntityList(List<UserDto> dtoList) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, User.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageDto toDtoPage(Page<User> entitiesPage) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entitiesPage.getContent()
                .stream().map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());
        return pageDto;
    }
}
