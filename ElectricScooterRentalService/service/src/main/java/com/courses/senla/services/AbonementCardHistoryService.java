package com.courses.senla.services;


import com.courses.senla.models.AbonementCardHistory;
import com.courses.senla.models.User;
import org.springframework.data.domain.Page;


public interface AbonementCardHistoryService {
    AbonementCardHistory save(AbonementCardHistory history);

    Page<AbonementCardHistory> getByUserLogin(Integer pageNo, Integer pageSize, String sortBy, User user);
}
