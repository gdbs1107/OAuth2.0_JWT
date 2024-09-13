package com.example.oauthjwt2.service.form;

import com.example.oauthjwt2.dto.UserDTO;
import com.example.oauthjwt2.entity.UserEntity;
import com.example.oauthjwt2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(UserDTO.JoinDTO request){

        UserEntity byEmail = userRepository.findByEmail(request.getEmail());

        //소셜로그인으로 가입된 계정인지 확인
        if(byEmail != null){
            throw new RuntimeException("이미 가입된 계정입니다");
        }

        UserEntity newUser = UserEntity.builder()
                .username(request.getUsername())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role("ROLE_USER")
                .name(request.getName())
                .build();

        userRepository.save(newUser);

    }
}
