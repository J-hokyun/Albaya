package com.example.albaya.store.controller;


import com.example.albaya.store.dto.DetailStoreDto;
import com.example.albaya.store.dto.StoreSaveDto;
import com.example.albaya.store.dto.StoreWithinCoordinatesDto;
import com.example.albaya.store.service.FileService;
import com.example.albaya.store.service.StoreService;
import com.example.albaya.store.dto.CoordinateDto;
import com.example.albaya.user.dto.UserInformDto;
import com.example.albaya.user.entity.User;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private final FileService fileService;

    @Value("${kakao_api_key}")
    private String kakaoApiKey;

    @GetMapping("/getStore")
    @ResponseBody
    public ResponseEntity<List<StoreWithinCoordinatesDto>> getStore(@RequestParam("northEastLat") double northEastLat, @RequestParam("northEastLng") double northEastLng,
                                                             @RequestParam("southWestLat") double southWestLat, @RequestParam("southWestLng") double southWestLng)
    {
        CoordinateDto coordinateDto = new CoordinateDto(northEastLat, northEastLng, southWestLat, southWestLng);
        List<StoreWithinCoordinatesDto> storeWithinCoordinatesDtos = storeService.findStoresWithinCoordinates(coordinateDto);
        for(StoreWithinCoordinatesDto dto : storeWithinCoordinatesDtos){
            System.out.println(dto);
        }
        return ResponseEntity.ok(storeWithinCoordinatesDtos);
    }

    @GetMapping("/detailStore/{storeId}")
    public String detailStore(@PathVariable Long storeId, Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInformDto userInformDto;
        if (principal == "anonymousUser"){
            userInformDto = UserInformDto.builder()
                    .loginStatus(false)
                    .build();
        }else{
            User user = (User)principal;
            userInformDto = UserInformDto.builder()
                    .loginStatus(true)
                    .name(user.getName())
                    .build();
        }
        DetailStoreDto detailStoreDto = storeService.detailStoreInform(storeId);

        model.addAttribute("informDto", userInformDto);
        model.addAttribute("storeDto", detailStoreDto);
        return "store/detailStore";
    }


    @GetMapping("/owner/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("uploadDto", new StoreSaveDto());
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        return "store/upload_store";
    }

    @PostMapping("/owner/upload")
    public String handleFileUpload(StoreSaveDto storeSaveDto) {
        log.info("request upload Store");

        // storeUploadDto.addr을 이용해 주소 API 호출
        Long storeId = storeService.saveStore(storeSaveDto);

        return "redirect:/detailStore/"+storeId;
    }
}
