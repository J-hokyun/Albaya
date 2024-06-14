package com.example.albaya.store.dto;

import com.example.albaya.enums.WorkType;
import com.example.albaya.store.entity.Store;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StoreFindResultDto {
    public Long store_id;
    public String store_name;
    public double area_lat;
    public double area_lng;
    public int store_salary;
    public String work_days;
    public LocalDateTime start_time;
    public LocalDateTime end_time;
    public WorkType type;


    public StoreFindResultDto toDto(Store store){
        return   StoreFindResultDto.builder()
                .store_id(store.getStore_id())
                .store_name(store.getStore_name())
                .area_lat(store.getArea_lat())
                .area_lng(store.getArea_lng())
                .store_salary(store.getStore_salary())
                .work_days(store.getWork_days())
                .start_time(store.getStart_time())
                .end_time(store.getEnd_time())
                .type(store.getType())
                .build();
    }

}
