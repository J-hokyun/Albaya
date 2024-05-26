package com.example.albaya.user.entity;

import com.example.albaya.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LikeStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int like_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private Date created_date;

    private Date updated_date;
}
