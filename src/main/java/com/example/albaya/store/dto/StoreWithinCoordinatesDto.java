package com.example.albaya.store.dto;

import com.example.albaya.enums.WorkType;
import com.example.albaya.store.entity.Store;
import com.example.albaya.store.entity.StoreImageUrl;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@RequiredArgsConstructor
@Getter
public class StoreWithinCoordinatesDto {
    private final Long storeId;
    private final String storeName;
    private final double areaLat;
    private final double areaLng;
    private final int storeSalary;
    private final String storeImageUrl;
    private final String workDays;
    private final LocalDateTime startTime;
    private final WorkType type;

    public StoreWithinCoordinatesDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.areaLat = store.getAreaLat();
        this.areaLng = store.getAreaLng();
        this.storeSalary = store.getStoreSalary();
        this.workDays = store.getWorkDays();
        this.startTime = store.getStartTime();
        this.type = store.getType();

        List<StoreImageUrl> imageUrlList = store.getStoreImageUrlList();
        if (!imageUrlList.isEmpty()){
            this.storeImageUrl = imageUrlList.get(0).getUrl(); // 추후에 썸네일 추가하면 수정
        }else{
            this.storeImageUrl = "https://albayabucket.s3.ap-northeast-2.amazonaws.com/default_store_image.png";
        }
    }

    @Override
    public String toString() {
        return "StoreWithinCoordinatesDto{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", areaLat=" + areaLat +
                ", areaLng=" + areaLng +
                ", storeSalary=" + storeSalary +
                ", storeImageUrl='" + storeImageUrl + '\'' +
                ", workDays='" + workDays + '\'' +
                ", startTime=" + startTime +
                ", type=" + type +
                '}';
    }
}
