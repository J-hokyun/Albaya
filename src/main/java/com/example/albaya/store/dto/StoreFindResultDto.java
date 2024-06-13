package com.example.albaya.store.dto;

import com.example.albaya.enums.WorkType;
import com.example.albaya.store.entity.Store;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@Builder
@ToString
public class StoreFindResultDto {

    public String store_name;
    public double area_lat;
    public double area_lng;
    private int store_salary;
    public String work_days;
    public LocalDateTime start_time;
    public LocalDateTime end_time;
    public WorkType type;

}
