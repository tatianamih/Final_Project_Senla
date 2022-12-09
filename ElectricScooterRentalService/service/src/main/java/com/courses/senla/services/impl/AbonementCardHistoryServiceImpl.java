package com.courses.senla.services.impl;


import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.models.AbonementCardHistory;
import com.courses.senla.models.User;
import com.courses.senla.repositories.AbonementCardHistoryRepository;
import com.courses.senla.services.AbonementCardHistoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AbonementCardHistoryServiceImpl implements AbonementCardHistoryService {

    private final AbonementCardHistoryRepository abonementCardHistoryRepository;

    @Autowired
    public AbonementCardHistoryServiceImpl(
            AbonementCardHistoryRepository abonementCardHistoryRepository) {
        this.abonementCardHistoryRepository = abonementCardHistoryRepository;
    }

    @Override
    public AbonementCardHistory save(AbonementCardHistory history) {
        return abonementCardHistoryRepository.save(history);
    }

    @Override
    public Page<AbonementCardHistory> getByUserLogin(Integer pageNo, Integer pageSize, String sortBy,
                                                     User user) {
        String username = user.getUsername();
        if (abonementCardHistoryRepository.existsByUser_Username(username)) {
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<AbonementCardHistory> histories = abonementCardHistoryRepository
                    .findAbonementCardHistoriesByUser_Username(paging, username);
            log.info("get histories by user username on page {}", pageNo);

            return histories;
        } else {
            log.error("user never had history with username {}", username);
            throw new ResourceNotFoundException("user never had history with username = ", username);
        }
    }
}
