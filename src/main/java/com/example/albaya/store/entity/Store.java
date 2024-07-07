package com.example.albaya.store.entity;

import com.example.albaya.enums.WorkType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "store")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id")
    private Long storeId;

    @ManyToOne
    @JoinColumn(name ="owner_id")
    private Owner owner;

    @Column(length = 100, nullable = false, name = "store_name")
    private String storeName;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StoreImageUrl> storeImageUrlList = new ArrayList<>();

    @Column(nullable = false, name = "area_lat")
    private double areaLat;

    @Column(nullable = false, name = "area_lng")
    private double areaLng;

    @Column(nullable = false, name = "store_salary")
    private int storeSalary;

    @Column(nullable = false, name = "work_days")
    private String workDays;

    @Column(nullable = false, name = "start_time")
    private LocalDateTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private WorkType type;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}