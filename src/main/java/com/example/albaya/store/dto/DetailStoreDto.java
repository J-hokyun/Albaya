package com.example.albaya.store.dto;

import com.example.albaya.enums.WorkType;
import com.example.albaya.store.entity.Store;
import com.example.albaya.store.entity.StoreImageUrl;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@RequiredArgsConstructor
@Getter
public class DetailStoreDto {

    @Value("${default_store_image_url}")
    private String default_store_image_url;
    private final Long storeId;
    private final String storeName;
    private final double areaLat;
    private final double areaLng;
    private final int storeSalary;
    private final List<String> storeImageUrlList;
    private final String workDays;
    private final LocalDateTime startTime;
    private final WorkType type;


    public DetailStoreDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.areaLat = store.getAreaLat();
        this.areaLng = store.getAreaLng();
        this.storeSalary = store.getStoreSalary();
        this.workDays = store.getWorkDays();
        this.startTime = store.getStartTime();
        this.type = store.getType();
        this.storeImageUrlList = new ArrayList<>();

        List<StoreImageUrl> imageUrlList = store.getStoreImageUrlList();
        if (imageUrlList!=null && !imageUrlList.isEmpty()){
            for (StoreImageUrl storeImageUrl : imageUrlList){
                this.storeImageUrlList.add(storeImageUrl.getUrl());
            }
        }else{
            this.storeImageUrlList.add("https://albayabucket.s3.ap-northeast-2.amazonaws.com/default_store_image.png");
        }
    }

    @Override
    public String toString() {
        return "StoreFindResultDto{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", areaLat=" + areaLat +
                ", areaLng=" + areaLng +
                ", storeSalary=" + storeSalary +
                ", storeImageUrlList=" + storeImageUrlList +
                ", workDays='" + workDays + '\'' +
                ", startTime=" + startTime +
                ", type=" + type +
                '}';
    }
}
