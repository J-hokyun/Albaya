package com.example.albaya.store.controller;

import com.example.albaya.store.dto.StoreFindResultDto;
import com.example.albaya.store.service.StoreService;
import com.example.albaya.store.dto.CoordinateDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
}
