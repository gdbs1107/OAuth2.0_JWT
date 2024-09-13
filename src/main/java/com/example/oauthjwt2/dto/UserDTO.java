package com.example.oauthjwt2.dto;

import lombok.*;

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
    }
}
