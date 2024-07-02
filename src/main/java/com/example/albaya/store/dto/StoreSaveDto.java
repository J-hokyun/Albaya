package com.example.albaya.store.dto;

import com.example.albaya.enums.Role;
import com.example.albaya.enums.WorkType;
import com.example.albaya.store.entity.Store;
import com.example.albaya.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@ToString
public class StoreSaveDto {

    private String store_name;
    private double area_lat;
    private double area_lng;
    private int store_salary;
    private String work_days;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private WorkType type;


    public Store toEntity(){
        return Store.builder()
                .store_name(store_name)
                .area_lat(area_lat)
                .area_lng(area_lng)
                .store_salary(store_salary)
                .work_days(work_days)
                .start_time(start_time)
                .end_time(end_time)
                .type(type)
                .created_date(LocalDateTime.now())
                .build();

    }
}

