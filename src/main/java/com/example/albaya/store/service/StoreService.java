package com.example.albaya.store.service;


import com.example.albaya.store.dto.CoordinateDto;
import com.example.albaya.store.dto.StoreFindResultDto;
import com.example.albaya.store.dto.StoreSaveDto;
import com.example.albaya.store.entity.Store;
import com.example.albaya.store.repository.StoreRepository;
import com.example.albaya.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final Logger logger = LoggerFactory.getLogger(StoreService.class);

    @Transactional
    public void storeSave(StoreSaveDto storeSaveDto){
        logger.info(storeSaveDto + "Save into Database");
        Store store = storeSaveDto.toEntity();
        storeRepository.save(store);
    }



    @Transactional
    public List<StoreFindResultDto> findStoresWithinBounds(CoordinateDto coordinateDto) {
        logger.info("find store in " + coordinateDto);
        List<Store> storeList = storeRepository.findStoresWithinBounds(
                coordinateDto.getNorthEastLat(),
                coordinateDto.getNorthEastLng(),
                coordinateDto.getSouthWestLat(),
                coordinateDto.getSouthWestLng()
        );

        List<StoreFindResultDto> storeFindResultDtoList = new ArrayList<>();
        for (Store store : storeList){
            StoreFindResultDto storeFindResultDto = StoreFindResultDto.builder()
                    .store_name(store.getStore_name())
                    .area_lat(store.getArea_lat())
                    .area_lng(store.getArea_lng())
                    .store_salary(store.getStore_salary())
                    .work_days(store.getWork_days())
                    .start_time(store.getStart_time())
                    .end_time(store.getEnd_time())
                    .type(store.getType())
                    .build();
            storeFindResultDtoList.add(storeFindResultDto);
        }
        logger.info("result counting : " + storeFindResultDtoList.size());
        return storeFindResultDtoList;
    }

}
