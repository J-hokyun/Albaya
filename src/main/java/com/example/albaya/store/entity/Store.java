package com.example.albaya.store.entity;

import com.example.albaya.enums.WorkType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int store_id;

    @ManyToOne
    @JoinColumn(name ="owner_id")
    private Owner owner;

    @Column(length = 100, nullable = false)
    private String store_name;

    private String store_picture_url;

    @Column(nullable = false)
    private String area_lat;

    @Column(nullable = false)
    private String area_lang;

    @Column(nullable = false)
    private String store_salary;

    @Column(nullable = false)
    private String work_days;

    @Column(nullable = false)
    private LocalTime start_time;

    @Column(nullable = false)
    private LocalTime end_time;

    @Enumerated(EnumType.STRING)
    private WorkType type;

    @Column(nullable = false)
    private Date created_date;

    private Date updated_date;
}

