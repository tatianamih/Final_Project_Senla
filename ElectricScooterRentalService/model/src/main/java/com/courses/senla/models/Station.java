package com.courses.senla.models;


import lombok.*;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, exclude = {"scooters", "city"})
@Entity(name = "Station")
@Table(name = "station")
public class Station extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "city_id")
    @ToString.Exclude
    private City city;

    @OneToMany(mappedBy = "station")
    @ToString.Exclude
    private List<Scooter> scooters;
}
