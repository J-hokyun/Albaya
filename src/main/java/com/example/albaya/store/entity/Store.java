package com.example.albaya.store.entity;

import com.example.albaya.enums.WorkType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long store_id;

    @ManyToOne
    @JoinColumn(name ="owner_id")
    private Owner owner;

    @Column(length = 100, nullable = false)
    private String store_name;

    private String store_picture_url;

    @Column(nullable = false)
    private double area_lat;

    @Column(nullable = false)
    private double area_lng;

    @Column(nullable = false)
    private int store_salary;

    @Column(nullable = false)
    private String work_days;

    @Column(nullable = false)
    private LocalDateTime start_time;

    @Column(nullable = false)
    private LocalDateTime end_time;

    @Enumerated(EnumType.STRING)
    private WorkType type;

    @Column(nullable = false)
    private LocalDateTime created_date;

    private LocalDateTime updated_date;
}

