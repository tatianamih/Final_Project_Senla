package com.courses.senla.models;

import com.courses.senla.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"scooterType", "bookings", "station"})
@Entity(name = "Scooter")
@Table(name = "scooter")
public class Scooter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "mileage")
    private Double mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "coordinate_x")
    private Double coordinateX;

    @Column(name = "coordinate_y")
    private Double coordinateY;

    @Column(name = "cost_per_min")
    private BigDecimal costPerHour;

    @ManyToOne
    @JoinColumn(name = "station_id")
    @ToString.Exclude
    private Station station;

    @ManyToOne
    @JoinColumn(name = "scooter_type_id")
    @ToString.Exclude
    private ScooterType scooterType;

    @OneToMany(cascade = {CascadeType.ALL},
            mappedBy = "scooter")
    @ToString.Exclude
    private List<Booking> bookings;
}

