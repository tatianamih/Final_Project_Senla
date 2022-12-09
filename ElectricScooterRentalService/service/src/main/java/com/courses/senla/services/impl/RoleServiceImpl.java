package com.courses.senla.services.impl;


import com.courses.senla.enums.ERole;
import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.exceptions.ResourceRepetitionException;
import com.courses.senla.models.Role;
import com.courses.senla.models.User;
import com.courses.senla.repositories.RoleRepository;
import com.courses.senla.services.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {

        this.repository = repository;
    }

    @Override
    public List<Role> getAll() {
        List<Role> roles = repository.findAll();
        if (roles.isEmpty()) {
            log.error("role not exist");
            throw new ResourceNotFoundException("roles not found");
        } else {
            log.info("roles were gotten");

            return roles;
        }
    }

    @Override
    public Role getById(Long id) {
        log.info("get a role by id {}", id);
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("role not found by id = ", id));
    }

    @Override
    public Role getByName(ERole name) {
        if (!repository.existsByName(name)) {
            log.error("role not exists with name -- {}", name);
            throw new ResourceNotFoundException("role not exist by name = ", name.toString());
        } else {
            log.info("get a role by name-- {}", name);

            return repository.findByName(name);
        }
    }
}


