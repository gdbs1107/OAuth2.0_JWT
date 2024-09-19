package com.example.oauthjwt2.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String username;
    private String name;
    private String role;


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO{

        private String username;
        private String password;
        private String email;
        private String name;

        //임시적인 회원 추가정보
        private String nickName;
        private LocalDateTime birthDate;
        private String phone;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO{
        private String username;
        private String password;
    }



    //소셜로그인 후 회원 세부정보 저장을 위한 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoDTO{

        private String username;
        private String password;
        private String email;
        private String name;

        //임시적인 회원 추가정보
        private String nickName;
        private LocalDateTime birthDate;
        private String phone;
    }
}
