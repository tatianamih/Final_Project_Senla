package com.courses.senla.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @CreationTimestamp
    @Column(name = "created")
    protected LocalDateTime created;

    @Column(name = "deleted",
    columnDefinition = "false")
    protected Boolean deleted;
}
