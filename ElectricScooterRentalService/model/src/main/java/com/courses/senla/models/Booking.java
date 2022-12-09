package com.courses.senla.models;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import com.courses.senla.enums.BookingStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
//???????
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"scooter","user"})
@ToString()
@Entity
@Table(name = "booking")
public class Booking{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "deleted",nullable = false, columnDefinition = "boolean default false")
    private Boolean deleted;

    @CreationTimestamp
    @Column(name = "start_rent_date",nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime startRentDate;

    @Column(name = "end_rent_date")
    private LocalDateTime endRentDate;

    @Column(name = "distance", nullable = false, columnDefinition = "double default 0.00")
    private Double distance;

    @Column(name = "cost", nullable = false, columnDefinition = "numeric(1000,2) default 0.00")
    @ColumnDefault("0.00")
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    @ColumnDefault("OPEN")
    private BookingStatus status;

    @ManyToOne()
    @JoinColumn(name = "user_profile_id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "scooter_id")
    @ToString.Exclude
    private Scooter scooter;
}