package com.example.albaya;

import com.example.albaya.enums.WorkType;
import com.example.albaya.store.dto.CoordinateDto;
import com.example.albaya.store.dto.StoreFindResultDto;
import com.example.albaya.store.dto.StoreSaveDto;
import com.example.albaya.store.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class StoreTest {

    @Autowired
    private StoreService storeService;

    @Test
    @DisplayName("Find Store Test using storeId")
    public void findStoreUsingStoreId(){
        StoreSaveDto storeSaveDto_1 = StoreSaveDto.builder()
                .store_name("store_test1")
                .area_lat(37.0600000)
                .area_lng(127.0500000)
                .store_salary(2000)
                .work_days("월-화")
                .start_time(LocalDateTime.now())
                .end_time(LocalDateTime.now())
                .type(WorkType.PART)
                .build();

        Long storeId = storeService.saveStore(storeSaveDto_1);

        StoreFindResultDto findResultDto = storeService.findStore(storeId);
        Assertions.assertEquals(storeId, findResultDto.getStore_id());
    }

    @Test
    @DisplayName("Find store test within bound")
    public void findStoreTestWithinBounder(){
        StoreSaveDto storeSaveDto_1 = StoreSaveDto.builder()
                .store_name("store_test1")
                .area_lat(37.0600000)
                .area_lng(127.0500000)
                .store_salary(2000)
                .work_days("월-화")
                .start_time(LocalDateTime.now())
                .end_time(LocalDateTime.now())
                .type(WorkType.PART)
                .build();

        StoreSaveDto storeSaveDto_2 = StoreSaveDto.builder()
                .store_name("store_test2")
                .area_lat(37.0700000)
                .area_lng(127.0700000)
                .store_salary(2000)
                .work_days("월-화")
                .start_time(LocalDateTime.now())
                .end_time(LocalDateTime.now())
                .type(WorkType.PART)
                .build();

        StoreSaveDto storeSaveDto_3 = StoreSaveDto.builder()
                .store_name("store_test3")
                .area_lat(37.0792107)
                .area_lng(127.0700000)
                .store_salary(2000)
                .work_days("월-화")
                .start_time(LocalDateTime.now())
                .end_time(LocalDateTime.now())
                .type(WorkType.PART)
                .build();

        CoordinateDto coordinateDto = CoordinateDto.builder()
                .northEastLat(37.0792105)
                .northEastLng(127.0835125)
                .southWestLat(37.0586649)
                .southWestLng(127.0416701)
                .build();

        storeService.saveStore(storeSaveDto_1);
        storeService.saveStore(storeSaveDto_2);
        storeService.saveStore(storeSaveDto_3);

        List<StoreFindResultDto>storeFindResultDtoList = storeService.findStoresWithinBounds(coordinateDto);
        Assertions.assertEquals(storeFindResultDtoList.size(), 2);
    }


}
