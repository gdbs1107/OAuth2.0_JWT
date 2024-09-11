package com.example.oauthjwt2.service;

import com.example.oauthjwt2.entity.UserEntity;
import com.example.oauthjwt2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    public String getName(String username) {

        UserEntity byUsername = userRepository.findByUsername(username);
        return byUsername.getName();
    }
}
