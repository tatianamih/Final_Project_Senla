package com.courses.senla.services.impl;

import com.courses.senla.enums.BookingStatus;
import com.courses.senla.models.Booking;
import com.courses.senla.models.Scooter;
import com.courses.senla.models.User;
import com.courses.senla.models.UserAccount;
import com.courses.senla.repositories.BookingRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.courses.senla.exceptions.ResourceNotFoundException;
import com.courses.senla.services.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Log4j2
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ScooterService scooterService;
    private final UserService userService;
    private final UserAccountService userAccountService;
    private final UserAccountHistoryService userAccountHistoryService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              ScooterService scooterService,
                              UserService userService, UserAccountService userAccountService,
                              UserAccountHistoryService userAccountHistoryService){
        this.bookingRepository = bookingRepository;
        this.scooterService = scooterService;
        this.userService = userService;
        this.userAccountService = userAccountService;
        this.userAccountHistoryService = userAccountHistoryService;
    }

    @Transactional
    @Override
    public Booking save(Booking booking) {
        User user = userService.getById(booking.getUser().getId());
        Scooter scooter = scooterService.getById(booking.getScooter().getId());
        booking.setUser(user);
        booking.setScooter(scooter);
        booking.setStatus(BookingStatus.OPEN);
        booking.setEndRentDate(LocalDateTime.now());
        booking.setStartRentDate(LocalDateTime.now());
        booking.setCost(BigDecimal.valueOf(0.00));
        booking.setDeleted(false);
        booking.setDistance(0.00);
        log.info("booking was saved");

        return bookingRepository.save(booking);
    }

    @Override
    public Page<Booking> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        log.info("get all bookings on page {}", pageNo);

        return bookingRepository.findAll(paging);
    }

    @Transactional
    @Override
    public Booking update(Booking booking, Long id) {
        if (getById(id).getStatus().equals(BookingStatus.CLOSE)) {
            log.error("booking not update with id -- {}", id);
            throw new ResourceNotFoundException("booking not update by id = ", id);
        } else {
            Booking updatedBooking = getById(id);
            log.info("update booking");
            User user = userService.getById(booking.getUser().getId());
            Scooter scooter = scooterService.getById(booking.getScooter().getId());
            updatedBooking.setUser(user);
            updatedBooking.setScooter(scooter);

            return bookingRepository.save(updatedBooking);
        }
    }

    @Override
    public Booking getById(Long id) {
        log.info("get a booking by id {}", id);
        return bookingRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("booking  not found by id = ", id));
    }

    @Transactional
    @Override
    public Booking finishBooking(Long id) {
        if (getById(id).getStatus().equals(BookingStatus.CLOSE)) {
            log.debug("booking was closed by id -- {}", id);
            throw new ResourceNotFoundException("booking was closed by id = ", id);
        } else {
            Booking updatedBooking = getById(id);
            log.info("booking");
            updateData(updatedBooking);
            accountBalanceRecalculation(updatedBooking);
            return updatedBooking;
        }
    }

    @Override
    public Page<Booking> getBookingsByScooterId(Integer pageNo, Integer pageSize, String sortBy, Long id) {
        if (scooterService.existById(id)) {
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<Booking> bookingPages = bookingRepository.findBookingsByScooter_Id(paging, id);
            log.debug("get all bookings on page {} by scooter id {} ", pageNo, id);

            return bookingPages;
        } else {
            log.error("bookings not found by scooter id {}", id);
            throw new ResourceNotFoundException("bookings not found by scooter id = ", id);
        }
    }

    @Override
    public List<Booking> getBookingsByDatesAndUser(LocalDateTime startDate,
                                                   LocalDateTime endData,
                                                   User user) {
        return bookingRepository.findBookingsByStartRentDateAndEndRentDateAndUser(startDate, endData,user);
    }

    private void updateData(Booking booking) {
        User user = userService.getById(booking.getUser().getId());
        Scooter scooter = scooterService.getById(booking.getScooter().getId());
        Double distance = getDistance(calculateSeconds(booking), scooter);
        booking.setUser(user);
        booking.setScooter(scooter);
        booking.setEndRentDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CLOSE);
        booking.setDistance(distance);
        booking.setCost(costRecalculation(booking,scooter));
        bookingRepository.save(booking);
        updateMileage(scooter,distance);
        log.info("update booking");
    }

    private Number calculateSeconds(Booking booking) {
        return ChronoUnit.SECONDS.between(booking.getStartRentDate(), LocalDateTime.now());
    }

    private Double getDistance(Number resultSeconds, Scooter scooter) {
        Double resultHours = resultSeconds.doubleValue() / 3600;
        Integer maxSpeeds = scooter.getScooterType().getMaxSpeed();
        double distance = maxSpeeds * resultHours;
        distance = Math.round(distance * 100.0) / 100.0;

        return distance;
    }

    private void updateMileage(Scooter scooter,Double distance){
        scooter.setMileage(scooter.getMileage() + distance);
        scooterService.update(scooter, scooter.getId());
    }

    private BigDecimal costRecalculation(Booking booking, Scooter scooter) {
        Number resultSeconds = calculateSeconds(booking);
        long resultMinutes = Math.round(resultSeconds.doubleValue() / 60);
        BigDecimal cost = new BigDecimal(resultMinutes).multiply(scooter.getCostPerHour());
        return cost.setScale(2, RoundingMode.HALF_UP);
    }

    private void accountBalanceRecalculation(Booking booking){
         User user = booking.getUser();
         UserAccount userAccount = user.getUserAccount();
         BigDecimal newBalance = userAccount.getBalance().subtract(booking.getCost())
                 .setScale(2, RoundingMode.HALF_UP);
         userAccount.setBalance(newBalance);
         userAccountService.update(userAccount, userAccount.getId());
         userAccountHistoryService.balanceHistoryRecalculation(userAccount, newBalance);
     }
}

