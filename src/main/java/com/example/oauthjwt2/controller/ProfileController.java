package com.example.oauthjwt2.controller;

import com.example.oauthjwt2.entity.UserEntity;
import com.example.oauthjwt2.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/")
    public String getName(@AuthenticationPrincipal UserEntity user) {

        String username = user.getUsername();
        return profileService.getName(username);
    }
}
