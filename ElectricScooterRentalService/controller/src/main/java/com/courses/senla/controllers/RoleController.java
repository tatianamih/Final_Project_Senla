package com.courses.senla.controllers;


import com.courses.senla.dto.RoleCreateDto;
import com.courses.senla.dto.RoleDto;
import com.courses.senla.mappers.impl.RoleMapper;
import com.courses.senla.models.Role;
import com.courses.senla.services.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/roles")
@Log4j2
@RestController
@Api(tags = "Role")
public class RoleController {
    private final RoleMapper mapper;

    private final RoleService service;

    @Autowired
    public RoleController(RoleMapper mapper,
                          RoleService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @ApiOperation("Найти все роли")
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDto>> getAll() {
        List<Role> roles = service.getAll();
        List<RoleDto> roleDtos = mapper.toDtoList(roles);

        return new ResponseEntity<>(roleDtos, HttpStatus.OK);
    }

    @ApiOperation("Найти роль по id")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> getById(@PathVariable Long id) {
        Role role = service.getById(id);
        RoleDto roleDto = mapper.toDto(role);

        return new ResponseEntity<>(roleDto, HttpStatus.OK);
    }
}