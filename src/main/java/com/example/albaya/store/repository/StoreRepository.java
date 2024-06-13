package com.example.albaya.store.repository;

import com.example.albaya.store.dto.CoordinateDto;
import com.example.albaya.store.entity.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s " +
            "WHERE s.area_lat BETWEEN :southWestLat AND :northEastLat " +
            "AND s.area_lng BETWEEN :southWestLng AND :northEastLng")
    List<Store> findStoresWithinBounds(
            @Param("northEastLat") double northEastLat,
            @Param("northEastLng") double northEastLng,
            @Param("southWestLat") double southWestLat,
            @Param("southWestLng") double southWestLng
    );

}

