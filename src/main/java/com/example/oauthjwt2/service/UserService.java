package com.example.oauthjwt2.service;

import com.example.oauthjwt2.dto.UserDTO;
import com.example.oauthjwt2.entity.UserEntity;
import com.example.oauthjwt2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;


    //소셜로그인 회원 세부정보 저장
    public void saveUserInfo(UserDTO.UserInfoDTO request,String username){

        UserEntity socialUser = userRepository.findByUsername(username);

        socialUser.setUserInfo(
                request.getNickName(),
                request.getBirthDate(),
                request.getPhone()
        );

        userRepository.save(socialUser);

    }
}
