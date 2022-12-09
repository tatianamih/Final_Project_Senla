package com.courses.senla.services;


import com.courses.senla.models.Booking;
import com.courses.senla.models.User;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking save(Booking booking);

    Page<Booking> getAll(Integer pageNo, Integer pageSize, String sortBy);

    Booking getById(Long id);

    Booking update(Booking booking, Long id);

    Booking finishBooking(Long bookingId);

    Page<Booking> getBookingsByScooterId(Integer pageNo, Integer pageSize, String sortBy, Long id);

    List<Booking> getBookingsByDatesAndUser(LocalDateTime startDate, LocalDateTime endData, User user);
}