package com.example.albaya.user.entity;

import com.example.albaya.enums.SchoolInform;
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
public class UserSchool {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_school_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100, nullable = false)
    private String school_name;

    @Column(nullable = false)
    private String school_address;

    @Enumerated(EnumType.STRING)
    private SchoolInform schoolInform;

    @Column(nullable = false)
    private Date created_date;

    private Date updated_date;
}
