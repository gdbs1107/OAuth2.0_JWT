package com.example.oauthjwt2.controller;

import com.example.oauthjwt2.dto.UserDTO;
import com.example.oauthjwt2.entity.UserEntity;
import com.example.oauthjwt2.service.ProfileService;
import com.example.oauthjwt2.service.form.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String join(@RequestBody UserDTO.JoinDTO request){

        joinService.join(request);
        return "success";
    }
}
