package com.courses.senla.repositories;


import com.courses.senla.models.Booking;
import com.courses.senla.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    Page<Booking> findBookingsByScooter_Id(Pageable paging, Long scooterId);

    List<Booking> findBookingsByStartRentDateAndEndRentDateAndUser(LocalDateTime startDate, LocalDateTime endData, User user);
}
