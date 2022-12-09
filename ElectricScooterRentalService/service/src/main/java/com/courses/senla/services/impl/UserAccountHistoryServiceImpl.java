package com.courses.senla.services.impl;


import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.models.User;
import com.courses.senla.models.UserAccount;
import com.courses.senla.models.UserAccountHistory;
import com.courses.senla.repositories.UserAccountHistoryRepository;
import com.courses.senla.services.UserAccountHistoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
public class UserAccountHistoryServiceImpl implements UserAccountHistoryService {

    private final UserAccountHistoryRepository historyRepository;

    @Autowired
    public UserAccountHistoryServiceImpl(UserAccountHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public UserAccountHistory save(UserAccountHistory history) {
        log.info("history was saved");

        return historyRepository.save(history);
    }

    @Override
    public Page<UserAccountHistory> getUserAccountHistoriesByDateAndUserAccount
            (Integer pageNo, Integer pageSize, String sortBy, Date date, User user) {
        if (!Objects.isNull(date)) {
            Instant instant = date.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDateTime date1 = zdt.toLocalDateTime();

            LocalDateTime date2 = LocalDateTime.of(date1.getYear(), date1.getMonth(), date1.getDayOfMonth(),
                    23, 59, 59);
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            return historyRepository.findUserAccountHistoriesByDateBetweenAndUserAccount
                    (paging, date1, date2, user.getUserAccount());
        } else {
            log.error("data isn't  existing,data -- {}", date);
            throw new ResourceNotFoundException
                    ("data isn't  existing,data = ", date);
        }
    }

    @Override
    public void balanceHistoryRecalculation(UserAccount userAccount, BigDecimal balance) {
        UserAccountHistory history = new UserAccountHistory();
        history.setUserAccount(userAccount);
        history.setSaldo(balance.setScale(2, RoundingMode.HALF_UP));
        save(history);
        List<UserAccountHistory> histories = userAccount.getHistories();
        histories.add(history);
    }
}

