package com.example.albaya.user.service;


import com.example.albaya.user.entity.UserDetails;
import com.example.albaya.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService {

    private final UserRepository userRepository;


    public UserDetails getUser(String email){
        return userRepository.findByEmail(email).orElse(null);
    }
}
