package com.example.albaya.store.dto;

import com.example.albaya.enums.WorkType;
import com.example.albaya.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@ToString
public class StoreSaveDto {

    private String storeName;
    private double areaLat;
    private double areaLng;
    private int storeSalary;
    private String workDays;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private WorkType type;


    public Store toEntity(){
        return Store.builder()
                .storeName(storeName)
                .areaLat(areaLat)
                .areaLng(areaLng)
                .storeSalary(storeSalary)
                .workDays(workDays)
                .startTime(startTime)
                .endTime(endTime)
                .type(type)
                .createdDate(LocalDateTime.now())
                .build();

    }
}

