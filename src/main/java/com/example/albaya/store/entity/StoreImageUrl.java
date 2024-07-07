package com.example.albaya.store.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class StoreImageUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_picture_id")
    private Long storePictureId;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;


}
