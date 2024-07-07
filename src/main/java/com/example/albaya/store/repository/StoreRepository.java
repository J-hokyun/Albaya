package com.example.albaya.store.repository;

import com.example.albaya.store.dto.CoordinateDto;
import com.example.albaya.store.entity.Store;
import com.example.albaya.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s " +
            "WHERE s.areaLat BETWEEN :southWestLat AND :northEastLat " +
            "AND s.areaLng BETWEEN :southWestLng AND :northEastLng")
    List<Store> findStoresWithinBounds(
            @Param("northEastLat") double northEastLat,
            @Param("northEastLng") double northEastLng,
            @Param("southWestLat") double southWestLat,
            @Param("southWestLng") double southWestLng
    );
    @Query("SELECT s FROM Store s " +
            "WHERE s.storeId = :storeId")
    Optional<Store> findByStoreId(@Param("storeId")Long storeId);

}

