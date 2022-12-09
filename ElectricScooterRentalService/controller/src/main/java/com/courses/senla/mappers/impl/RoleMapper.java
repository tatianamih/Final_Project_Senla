package com.courses.senla.mappers.impl;

import com.courses.senla.dto.PageDto;
import com.courses.senla.dto.RoleCreateDto;
import com.courses.senla.dto.RoleDto;
import com.courses.senla.mappers.GenericMapper;
import com.courses.senla.models.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for converting the Role entity to / from DTO
 */
@Component
public class RoleMapper implements GenericMapper<Role, RoleDto, RoleCreateDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoleDto toDto(Role role) {
        return Objects.isNull(role) ? null : modelMapper.map(role, RoleDto.class);
    }

    @Override
    public Role toEntity(RoleDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Role.class);
    }

    @Override
    public Role toEntityMethCreate(RoleCreateDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Role.class);
    }

    @Override
    public List<RoleDto> toDtoList(List<Role> entityList) {
        return entityList.stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> toEntityList(List<RoleDto> dtoList) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, Role.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageDto toDtoPage(Page<Role> entitiesPage) {
        PageDto pageDto = new PageDto();
        pageDto.setList(entitiesPage.getContent()
                .stream().map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toCollection(ArrayList::new)));
        pageDto.setTotalPages(entitiesPage.getTotalPages());
        return pageDto;
    }
}
