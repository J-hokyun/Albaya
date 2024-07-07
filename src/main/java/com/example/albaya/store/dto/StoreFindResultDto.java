package com.example.albaya.store.dto;

import com.example.albaya.enums.WorkType;
import com.example.albaya.store.entity.Store;
import com.example.albaya.store.entity.StoreImageUrl;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StoreFindResultDto {
    public Long storeId;
    public String storeName;
    public double areaLat;
    public double areaLng;
    public int storeSalary;
    public List<StoreImageUrl> storeImageUrlList;
    public String workDays;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public WorkType type;


    public StoreFindResultDto toDto(Store store){
        return   StoreFindResultDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .areaLat(store.getAreaLat())
                .areaLng(store.getAreaLng())
                .storeSalary(store.getStoreSalary())
                .storeImageUrlList(store.getStoreImageUrlList())
                .workDays(store.getWorkDays())
                .startTime(store.getStartTime())
                .endTime(store.getEndTime())
                .type(store.getType())
                .build();
    }

}
