package com.courses.senla.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"userAccount"})
@ToString
@Entity
@Table(name = "user_account_history")

public class UserAccountHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "saldo")
    private BigDecimal saldo;

    @ManyToOne()
    @JoinColumn(name = "user_account_id")
    @ToString.Exclude
    private UserAccount userAccount;
}
