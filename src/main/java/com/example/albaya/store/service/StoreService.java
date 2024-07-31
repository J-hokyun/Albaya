package com.example.albaya.store.service;


import com.example.albaya.store.dto.CoordinateDto;
import com.example.albaya.store.dto.StoreFindResultDto;
import com.example.albaya.store.dto.StoreSaveDto;
import com.example.albaya.store.entity.Store;
import com.example.albaya.store.entity.StoreImageUrl;
import com.example.albaya.store.repository.StoreImageUrlRepository;
import com.example.albaya.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    @Value("${kakao_api_key}")
    private String kakaoApiKey;

    private final StoreRepository storeRepository;
    private final FileService fileService;
    private final StoreImageUrlRepository storeImageUrlRepository;

    @Transactional
    public Long saveStore(StoreSaveDto storeSaveDto){
        Map<String, Double> coordinates = getCoordinate(storeSaveDto.getAddress());
        Store store = storeSaveDto.toEntity();
        store.setAreaLat(coordinates.get("areaLat"));
        store.setAreaLng(coordinates.get("areaLng"));
        Store savedStore = storeRepository.save(store);


        if (storeSaveDto.getStoreImageFiles() != null){
            List<String> imageUrlList = fileService.saveFiles(storeSaveDto.getStoreImageFiles());
            for (String url : imageUrlList){
                StoreImageUrl storeImageUrl = new StoreImageUrl();
                storeImageUrl.setStore(savedStore);
                storeImageUrl.setUrl(url);
                storeImageUrl.setCreatedDate(LocalDateTime.now());
                storeImageUrlRepository.save(storeImageUrl);
            }
        }
        return savedStore.getStoreId();
    }


    public Map<String, Double> getCoordinate(String addr){
        Map<String, Double> coordinates = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", addr)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();
        if (body != null && body.containsKey("documents")) {
            List<Map<String, Object>> documents = (List<Map<String, Object>>) body.get("documents");
            if (!documents.isEmpty()) {
                Map<String, Object> document = documents.get(0);
                coordinates.put("areaLng", Double.parseDouble(document.get("x").toString()));
                coordinates.put("areaLat", Double.parseDouble(document.get("y").toString()));
            } else {
                log.error("No coordinates found for the address");
            }
        } else {
            log.error("Error fetching coordinates: response body is null or empty");
        }
        return coordinates;
    }

    @Transactional(readOnly = true)
    public StoreFindResultDto findStore(Long storeId){
        Store store = storeRepository.findByStoreId(storeId).orElse(null);
        StoreFindResultDto storeFindResultDto = new StoreFindResultDto().toDto(store);

        if (store.getStoreImageUrlList() != null) {
            for (StoreImageUrl storeImageUrl : store.getStoreImageUrlList()) {
                String url = fileService.getFileUrl(storeImageUrl.getUrl());
                storeFindResultDto.getStoreImageUrlList().add(url);
            }
        }

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
