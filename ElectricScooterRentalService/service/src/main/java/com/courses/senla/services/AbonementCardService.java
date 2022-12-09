package com.courses.senla.services;


import com.courses.senla.models.AbonementCard;
import com.courses.senla.models.User;
import org.springframework.data.domain.Page;

public interface AbonementCardService {
    AbonementCard save(AbonementCard abonementCard);

    Page<AbonementCard> getAll(Integer pageNo, Integer pageSize, String sortBy);

    AbonementCard getById(Long id);

    AbonementCard update(AbonementCard abonementCard, Long id);

    AbonementCard buyAbonementCard(Long cardId, User user);
}
