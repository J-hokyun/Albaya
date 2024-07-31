package com.example.albaya;

import com.example.albaya.enums.WorkType;
import com.example.albaya.store.dto.CoordinateDto;
import com.example.albaya.store.dto.StoreFindResultDto;
import com.example.albaya.store.dto.StoreSaveDto;
import com.example.albaya.store.entity.Store;
import com.example.albaya.store.repository.StoreRepository;
import com.example.albaya.store.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class StoreTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("Find Store Test using storeId")
    public void findStoreUsingStoreId(){
        StoreSaveDto storeSaveDto = StoreSaveDto.builder()
                .storeName("storeTestName1")
                .address("서울특별시 종로구 세종대로 172")
                .storeSalary(10)
                .workDays("월-화")
                .startTime(LocalDateTime.now())
                .type("FULL")
                .storeImageFiles(new ArrayList<>())
                .build();

        Long storeId = storeService.saveStore(storeSaveDto);
        StoreFindResultDto findStoreDto = storeService.findStore(storeId);

        Assertions.assertEquals(storeId, findStoreDto.getStoreId());
    }

    @Test
    @DisplayName("Find store test within bound")
    public void findStoreTestWithinBounder(){

        Store store1 = Store.builder()
                .storeName("storeTest1")
                .areaLat(37.0600000)
                .areaLng(127.0500000)
                .storeImageUrlList(null)
                .storeSalary(10)
                .workDays("월-화")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .type(WorkType.FULL)
                .createdDate(LocalDateTime.now())
                .build();

        Store store2 = Store.builder()
                .storeName("storeTest1")
                .areaLat(37.0700000)
                .areaLng(127.0700000)
                .storeImageUrlList(null)
                .storeSalary(10)
                .workDays("월-화")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .type(WorkType.FULL)
                .createdDate(LocalDateTime.now())
                .build();

        Store store3 = Store.builder()
                .storeName("storeTest1")
                .areaLat(37.0800000)
                .areaLng(127.0900000)
                .storeImageUrlList(null)
                .storeSalary(10)
                .workDays("월-화")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .type(WorkType.FULL)
                .createdDate(LocalDateTime.now())
                .build();

        Store store4 = Store.builder()
                .storeName("storeTest1")
                .areaLat(37.0550000)
                .areaLng(127.0350000)
                .storeImageUrlList(null)
                .storeSalary(10)
                .workDays("월-화")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .type(WorkType.FULL)
                .createdDate(LocalDateTime.now())
                .build();

        CoordinateDto coordinateDto = CoordinateDto.builder()
                .northEastLat(37.0792105)
                .northEastLng(127.0835125)
                .southWestLat(37.0586649)
                .southWestLng(127.0416701)
                .build();

        storeRepository.save(store1);
        storeRepository.save(store2);
        storeRepository.save(store3);
        storeRepository.save(store4);

        List<StoreFindResultDto>storeFindResultDtoList = storeService.findStoresWithinBounds(coordinateDto);
        Assertions.assertEquals(storeFindResultDtoList.size(), 2);
    }


}
