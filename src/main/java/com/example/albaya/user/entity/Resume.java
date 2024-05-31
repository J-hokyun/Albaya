package com.example.albaya.user.entity;

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
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long resume_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String introduce;

    @Column(nullable = false)
    private Date created_date;

    private Date updated_date;

}
