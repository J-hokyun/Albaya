package com.example.albaya.store.entity;

import com.example.albaya.enums.Permit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long owner_id;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Store> storeList = new ArrayList<>();

    @Column(length = 50, nullable = false)
    private String store_code;

    @Enumerated(EnumType.STRING)
    private Permit permit;

    @Column(nullable = false)
    private Date created_date;

    private Date updated_date;
}
