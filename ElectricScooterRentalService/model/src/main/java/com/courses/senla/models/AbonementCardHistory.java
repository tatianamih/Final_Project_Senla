package com.courses.senla.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@Entity
@Table(name = "abonement_card_history")
public class AbonementCardHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "abonement_cost")
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "abonement_card_id")
    @ToString.Exclude
    @Fetch(FetchMode.JOIN)
    private AbonementCard abonementCard;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    @ToString.Exclude
    @Fetch(FetchMode.JOIN)
    private User user;
}

