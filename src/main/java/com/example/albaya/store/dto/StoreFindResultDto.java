package com.example.albaya.store.dto;

import com.example.albaya.enums.WorkType;
import com.example.albaya.store.entity.Store;
import com.example.albaya.store.entity.StoreImageUrl;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreFindResultDto {
    public Long storeId;
    public String storeName;
    public double areaLat;
    public double areaLng;
    public int storeSalary;
    public List<String> storeImageUrlList;
    public String workDays;
    public LocalDateTime startTime;
    public WorkType type;


    public StoreFindResultDto toDto(Store store){
        return   StoreFindResultDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .areaLat(store.getAreaLat())
                .areaLng(store.getAreaLng())
                .storeSalary(store.getStoreSalary())
                .workDays(store.getWorkDays())
                .startTime(store.getStartTime())
                .type(store.getType())
                .storeImageUrlList(new ArrayList<>())
                .build();
    }

    @Override
    public String toString() {
        return "StoreFindResultDto{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
