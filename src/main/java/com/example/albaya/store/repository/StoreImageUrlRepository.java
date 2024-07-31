package com.example.albaya.store.repository;

import com.example.albaya.store.entity.StoreImageUrl;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreImageUrlRepository extends JpaRepository<StoreImageUrl, Long> {

}
