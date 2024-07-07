package com.example.albaya.user.entity;

import com.example.albaya.enums.SchoolInform;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserSchool {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_school_id")
    private Long userSchoolId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100, nullable = false, name = "school_name")
    private String schoolName;

    @Column(nullable = false, name = "school_address")
    private String schoolAddress;

    @Enumerated(EnumType.STRING)
    private SchoolInform schoolInform;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
