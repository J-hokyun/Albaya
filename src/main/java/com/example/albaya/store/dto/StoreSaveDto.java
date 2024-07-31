package com.example.albaya.store.dto;

import com.example.albaya.enums.WorkType;
import com.example.albaya.store.entity.Store;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreSaveDto {
    private String storeName;
    private String address;
    private int storeSalary;
    private String workDays;
    private LocalDateTime startTime;
    private String type;
    private List<MultipartFile> storeImageFiles;


    @Override
    public String toString() {
        return "StoreUploadDto{" +
                "storeName='" + storeName + '\'' +
                ", address='" + address + '\'' +
                ", storeSalary=" + storeSalary +
                ", workDays='" + workDays + '\'' +
                ", startTime=" + startTime +
                ", type='" + type + '\'' +
                ", imageFilesCount=" + storeImageFiles.size() +
                '}';
    }

    public Store toEntity(){
        Store store = Store.builder()
                .storeName(this.storeName)
                .storeSalary(this.storeSalary)
                .workDays(this.workDays)
                .startTime(this.startTime)
                .endTime(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();

        if (this.type.equals("FULL"))
            store.setType(WorkType.FULL);
        else
            store.setType(WorkType.PART);

        return store;
    }
}
