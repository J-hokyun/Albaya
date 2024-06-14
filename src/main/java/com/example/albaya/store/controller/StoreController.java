package com.example.albaya.store.controller;


import com.example.albaya.store.dto.StoreFindResultDto;
import com.example.albaya.store.service.StoreService;
import com.example.albaya.store.dto.CoordinateDto;
import com.example.albaya.user.dto.UserInformDto;
import com.example.albaya.user.entity.User;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping("/getStore")
    @ResponseBody
    public ResponseEntity<List<StoreFindResultDto>> getStore(@RequestParam("northEastLat") double northEastLat, @RequestParam("northEastLng") double northEastLng,
                                                             @RequestParam("southWestLat") double southWestLat, @RequestParam("southWestLng") double southWestLng)
    {
        CoordinateDto coordinateDto = new CoordinateDto(northEastLat, northEastLng, southWestLat, southWestLng);
        List<StoreFindResultDto> storeList = storeService.findStoresWithinBounds(coordinateDto);
        return ResponseEntity.ok(storeList);
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
        StoreFindResultDto storeFindResultDto = storeService.findStore(storeId);

        model.addAttribute("informDto", userInformDto);
        model.addAttribute("storeDto", storeFindResultDto);
        return "store/detailStore";
    }
}
