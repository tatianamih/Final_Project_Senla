package com.courses.senla.services.impl;


import com.courses.senla.enums.BookingStatus;
import com.courses.senla.models.*;
import com.courses.senla.repositories.AbonementCardRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.courses.senla.exceptions.NegativeAccountException;
import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.exceptions.ResourceRepetitionException;
import com.courses.senla.services.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Log4j2
@Service
public class AbonementCardServiceImpl implements AbonementCardService {
    private final AbonementCardRepository repository;

    private final UserAccountService userAccountService;

    private final UserAccountHistoryService historyService;

    private final DiscountService discountService;

    private final AbonementCardHistoryService abonementCardHistory;

    private final BookingService bookingService;

    private final UserService userService;

    @Value("${distance.lower.limit}")
    private Double distanceLowerLimit;
    @Value("${distance.upper.limit}")
    private Double distanceUpperLimit;
    @Value("${percent.lower.limit}")
    Double percentLowerLimit;
    @Value("${percent.upper.limit}")
    Double percentUpperLimit;
    @Value("${number.discount.day}")
    Integer numberDiscountDay;

    @Autowired
    public AbonementCardServiceImpl(AbonementCardRepository repository,
                                    UserAccountService userAccountService,
                                    UserAccountHistoryService historyService,
                                    DiscountService discountService,
                                    AbonementCardHistoryService abonementCardHistory,
                                    BookingService bookingService, UserService userService) {

        this.repository = repository;
        this.userAccountService = userAccountService;
        this.historyService = historyService;
        this.discountService = discountService;
        this.abonementCardHistory = abonementCardHistory;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @Override
    public AbonementCard save(AbonementCard abonementCard) {
        if (repository.existsByName(abonementCard.getName())) {
            log.error("abonement card already exists with name -- {}", abonementCard.getName());
            throw new ResourceRepetitionException("such an abonement card already exists with name = ",
                    abonementCard.getName());
        } else {
            log.info("abonement card was saved");
            abonementCard.setAbonementCardBalance(abonementCard.getAbonementCardBalance());
            abonementCard.setAbonementCardCost(abonementCard.getAbonementCardBalance());

            return repository.save(abonementCard);
        }
    }

    @Override
    public Page<AbonementCard> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        log.info("get all abonement cards on page {}", pageNo);

        return repository.findAll(paging);
    }

    @Override
    public AbonementCard getById(Long id) {
        log.info("get an abonement card by id {}", id);

        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("abonement card not found by id = ", id));
    }

    @Override
    public AbonementCard update(AbonementCard abonementCard, Long id) {
        AbonementCard updateAbonementCard = getById(id);
        log.info("update abonement card, name -- {}", abonementCard.getName());
        updateAbonementCard.setName(abonementCard.getName());
        updateAbonementCard.setDeleted(abonementCard.getDeleted());
        updateAbonementCard.setAbonementCardCost(abonementCard.getAbonementCardCost());
        updateAbonementCard.setAbonementCardBalance(abonementCard.getAbonementCardBalance());

        return repository.save(updateAbonementCard);
    }

    @Transactional
    @Override
    public AbonementCard buyAbonementCard(Long cardId, User user) {
        AbonementCard card = getById(cardId);
        UserAccount userAccount = userService.getUserAccountByUserId(user.getId());
        BigDecimal userAccountBalance = userAccount.getBalance();
        if(isNotDeletedAbonementCard(card) && isPositiveBalance(userAccountBalance)){
            BigDecimal balanceWithDiscount = recalculationCostWithDiscount(user, card, numberDiscountDay);
            updateCardHistory(user, card);
            updateAccountHistory(user, balanceWithDiscount);
        }
        return card;
    }

    private void updateCardHistory(User user, AbonementCard card) {
        List<AbonementCardHistory> cardHistories = card.getHistories();
        BigDecimal cardBalance = card.getAbonementCardBalance();
        AbonementCardHistory cardHistory = addAbonementCardHistory(card, cardBalance, user);
        cardHistories.add(cardHistory);
    }

    private void updateAccountHistory(User user, BigDecimal balanceWithDiscount) {
        UserAccount userAccount = userService.getUserAccountByUserId(user.getId());
        BigDecimal newBalance = recalculationBalance(userAccount, balanceWithDiscount);
        List<UserAccountHistory> histories = addAccountHistories(userAccount, newBalance);
        userAccount.setHistories(histories);
    }

    private BigDecimal recalculationBalance(UserAccount userAccount, BigDecimal balanceWithDiscount) {
        BigDecimal userAccountBalance = userAccount.getBalance();
        BigDecimal newBalance = userAccountBalance.add(balanceWithDiscount);
        userAccountService.updateBalance(userAccount, userAccount.getId(), newBalance);
        return newBalance;
    }

    private UserAccountHistory addAccountHistory(UserAccount userAccount, BigDecimal newBalance) {
        UserAccountHistory history = new UserAccountHistory();
        history.setSaldo(newBalance);
        history.setDate(LocalDateTime.now());
        history.setUserAccount(userAccount);
        historyService.save(history);
        return history;
    }

    private List<UserAccountHistory> addAccountHistories(UserAccount userAccount, BigDecimal newBalance) {
        UserAccountHistory history = addAccountHistory(userAccount, newBalance);
        List<UserAccountHistory> histories = userAccount.getHistories();
        histories.add(history);
        return histories;
    }

    private boolean isPositiveBalance(BigDecimal userAccountBalance) {
        if (userAccountBalance.compareTo(BigDecimal.valueOf(0.00)) > 0) {
            return true;
        } else {
            log.info("the account is negative");
            throw new NegativeAccountException("the account is negative");
        }
    }

    private void recalculationDiscount(User user, int numberDiscountDay) {
        LocalDateTime startDate = getStartDate();
        LocalDateTime endDate = getEndDate();
        List<Booking> bookings = bookingService
                .getBookingsByDatesAndUser(startDate, endDate, user);
        Double distance =calculateDistanceForUserByMonth(user, bookings);
        List<Discount> discounts = discountService.getDiscountsByUserId(user.getId());
        Discount discount = discounts.get(0);
        updateDiscount(discount, distance);
        resetDiscounts(discounts, numberDiscountDay);
    }

    private Boolean isNotDeletedAbonementCard(AbonementCard card) {
        if (card.getDeleted().equals(false)) {
            return true;
        } else {
            log.info("abonement card is deleted");
            throw new ResourceNotFoundException("abonement card is deleted by id {}", card.getId());
        }
    }

    private BigDecimal recalculationCostWithDiscount(User user, AbonementCard card, Integer numberDiscountDay) {
        recalculationDiscount(user,numberDiscountDay);
        List<Discount> discounts = discountService.getDiscountsByUserId(user.getId());
        Double percent = discountService.calculationPercentsSum(discounts);
        double coefficient = (percent / 100) + 1;
        BigDecimal newBalance = BigDecimal.valueOf(coefficient)
                .multiply(card.getAbonementCardBalance());

        return newBalance.setScale(2, RoundingMode.HALF_UP);
    }

    private AbonementCardHistory addAbonementCardHistory(AbonementCard card, BigDecimal cardCost, User user) {
        AbonementCardHistory cardHistory = new AbonementCardHistory();
        cardHistory.setAbonementCard(card);
        cardHistory.setCost(cardCost);
        cardHistory.setUser(user);
        abonementCardHistory.save(cardHistory);

        return cardHistory;
    }

    //FIXME использовать стримы
    private Double calculateDistanceForUserByMonth(User user, List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> BookingStatus.CLOSE.equals(b.getStatus()))
                .filter(b -> user.equals(b.getUser()))
                .map(Booking::getDistance)
                .collect(Collectors.toList())
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private LocalDateTime getStartDate() {
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime startDate;
        if (date.getMonthValue() > 1) {
            startDate = LocalDateTime.of(date.getYear(),
                    date.getMonth().minus(1), 1, 0, 0, 0);
        } else {
            startDate = LocalDateTime.of(date.getYear() - 1, 12, 1, 0, 0, 0);
        }
        return startDate;
    }

    private LocalDateTime getEndDate() {
        LocalDateTime endDate;
        LocalDateTime startDate = getStartDate();
        endDate = startDate.with(lastDayOfMonth());
        endDate = LocalDateTime.of(endDate.getYear(),
                endDate.getMonth(),
                endDate.getDayOfMonth(),
                23, 59, 59);
        return endDate;
    }

    private void updateDiscount(Discount discount, Double distance) {
        if (distance > distanceUpperLimit && !isFirstDayOfMonth()) {
            discount.setPercent(percentUpperLimit);
        } else if (distance >= distanceUpperLimit && distance < distanceLowerLimit && !isFirstDayOfMonth()) {
            discount.setPercent(percentLowerLimit);
        } else {
            discount.setPercent(0.00);
        }
        discountService.update(discount, discount.getId());
    }

    private void resetDiscount(Discount discount, int numberDiscountDay) {
        LocalDate nowDate = LocalDate.now();//6.12.22
        LocalDateTime created = discount.getCreated();//1.12.22 diff = 10; n = 15
        Period period = Period.between(LocalDate.from(created), nowDate);
        int dayDifferent = Math.abs(period.getDays());
        if (dayDifferent > numberDiscountDay) {
            discount.setPercent(0.00);
        }
    }

    private void resetDiscounts(List<Discount> discounts, int numberDiscountDay) {
        for (int i = 1; i < discounts.size(); i++) {
            resetDiscount(discounts.get(i), numberDiscountDay);

        }
    }

    private Boolean isFirstDayOfMonth(){
        LocalDateTime dateTime = LocalDateTime.now();
        int dayOfMonth = dateTime.getDayOfMonth();
        return 1 == dayOfMonth;
    }
}



