package com.example.albaya.member.dto;

import com.example.albaya.member.entity.Resume;
import com.example.albaya.user.dto.UserInformDto;
import com.example.albaya.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class WriteResumeDto extends UserInformDto {


    private String title;
    private String education;
    private List<String> desiredLocationList;
    private String employmentType;
    private String workDuration;
    private String workDays;
    private String introduce;
    private Boolean disability;
    private Boolean militaryStatus;
    private Boolean veteranStatus;


    public WriteResumeDto(){
        super();
    }
    public WriteResumeDto(User user) {
        super(user);
    }


    public Resume toEntity(User user){
        return Resume.builder()
                .user(user)
                .title(this.title)
                .education(this.education)
                .desiredLocationList(String.join(", ",this.desiredLocationList))
                .employmentType(this.employmentType)
                .workDuration(this.workDuration)
                .workDays(this.workDays)
                .introduce(this.introduce)
                .disability(this.disability)
                .militaryStatus(this.militaryStatus)
                .veteranStatus(this.veteranStatus)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
