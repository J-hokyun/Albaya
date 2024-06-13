package com.example.albaya.store.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@ToString
public class CoordinateDto {
    public double northEastLat;
    public double northEastLng;
    public double southWestLat;
    public double southWestLng;
}
