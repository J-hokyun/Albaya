package com.example.albaya.store.service;


import com.example.albaya.store.dto.CoordinateDto;
import com.example.albaya.store.dto.StoreFindResultDto;
import com.example.albaya.store.dto.StoreSaveDto;
import com.example.albaya.store.entity.Store;
import com.example.albaya.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public Long saveStore(StoreSaveDto storeSaveDto){
        log.info(storeSaveDto + "Save into Database");
        Store store = storeSaveDto.toEntity();
        storeRepository.save(store);

        return store.getStore_id();
    }

    @Transactional(readOnly = true)
    public StoreFindResultDto findStore(Long storeId){
        Store store = storeRepository.findByStoreId(storeId).orElse(null);
        StoreFindResultDto storeFindResultDto = new StoreFindResultDto().toDto(store);
        log.info("find storeId: " + storeId + " " + storeFindResultDto);
        return storeFindResultDto;

    }



    @Transactional
    public List<StoreFindResultDto> findStoresWithinBounds(CoordinateDto coordinateDto) {
        log.info("find store in " + coordinateDto);
        List<Store> storeList = storeRepository.findStoresWithinBounds(
                coordinateDto.getNorthEastLat(),
                coordinateDto.getNorthEastLng(),
                coordinateDto.getSouthWestLat(),
                coordinateDto.getSouthWestLng()
        );

        List<StoreFindResultDto> storeFindResultDtoList = new ArrayList<>();
        for (Store store : storeList){
            storeFindResultDtoList.add(new StoreFindResultDto().toDto(store));
        }
        log.info("result counting : " + storeFindResultDtoList.size());
        return storeFindResultDtoList;
    }

}
