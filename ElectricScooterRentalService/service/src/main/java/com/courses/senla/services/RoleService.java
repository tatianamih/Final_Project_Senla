package com.courses.senla.services;


import com.courses.senla.enums.ERole;
import com.courses.senla.models.Role;
import com.courses.senla.models.User;

import java.util.List;

public interface RoleService {

    List<Role> getAll();

    Role getById(Long id);

    Role getByName(ERole name);
}
