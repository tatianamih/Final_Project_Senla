package com.courses.senla.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
@Entity
@Table(name = "scooter_type")
public class ScooterType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "max_speed")
    private Integer maxSpeed;

    @OneToMany(mappedBy = "scooterType")
    @ToString.Exclude
    private List<Scooter> scooters;
}
