package com.example.albaya.member.entity;

import com.example.albaya.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "resume_id")
    private Long resumeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false)
    private String education;

    @Column(nullable = false)
    private String desiredLocationList;

    @Column(nullable = false)
    private String employmentType;

    @Column(nullable = false)
    private String workDuration;

    @Column(nullable = false)
    private String workDays;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String introduce;

    @Column(nullable = false)
    private Boolean disability;

    @Column(nullable = false)
    private Boolean militaryStatus;

    @Column(nullable = false)
    private Boolean veteranStatus;



    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

}
