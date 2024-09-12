package com.example.oauthjwt2.controller;

import com.example.oauthjwt2.dto.AccessDTO;
import com.example.oauthjwt2.service.OAuth2JWTHeaderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/*OAuth소셜로그인 특성상, 로그인을 진행할때 하이퍼링크를 통해 진행되고
    이러면 토큰을 바로 전달 할 수 없음
그렇기 때문에 로그인 성공시 바로 해당 엔드포인트로 리다이렉트 되도록 설정하여
        다시 access-> 헤더로 전달 할 수 있도록 구현
        그리고 프론트 단에서는 이렇게 전달된 access를 로컬스토리지에 저장하여 사용*/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/")
public class OAuth2Controller {

    private final OAuth2JWTHeaderService oAuth2JwtHeaderService;


    @PostMapping("/oauth2-jwt-header")
    public AccessDTO oauth2JwtHeader(HttpServletRequest request, HttpServletResponse response) {
        log.info("oauth2-jwt-header 컨트롤러가 실행됩니다");
        return oAuth2JwtHeaderService.oauth2JwtHeaderSet(request, response);
    }
}