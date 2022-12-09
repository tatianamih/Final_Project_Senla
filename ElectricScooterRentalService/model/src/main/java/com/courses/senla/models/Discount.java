package com.courses.senla.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "discounts")
public class Discount extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "percent")
    private Double percent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}