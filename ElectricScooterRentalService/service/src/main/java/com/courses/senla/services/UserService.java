package com.courses.senla.services;


import com.courses.senla.models.Role;
import com.courses.senla.models.User;
import com.courses.senla.models.UserAccount;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface UserService {
    User userRegistration(User user);

    User getById(Long id);

    Page<User> getAll(Integer pageNo, Integer pageSize, String sortBy);

    User update(User user, Long id);

    User getByUsername(String username);

    UserAccount getUserAccountByUserId(Long id);

    UserAccount replenishBalance(Long id, BigDecimal sum);

    User addRole(Long id);
}

