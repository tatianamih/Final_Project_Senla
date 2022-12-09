package com.courses.senla.models;


import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, exclude = {"histories"})
@Entity
@Table(name = "abonement_card")
public class AbonementCard extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name="deleted")
    private Boolean deleted;

    @Column(name = "abonement_card_cost")
    private BigDecimal abonementCardCost;

    @Column(name = "abonement_card_balance")
    private BigDecimal abonementCardBalance;

    @OneToMany(cascade = {CascadeType.ALL},
            mappedBy = "abonementCard")
    @Fetch(FetchMode.JOIN)
    @ToString.Exclude
    private List<AbonementCardHistory> histories;

}
