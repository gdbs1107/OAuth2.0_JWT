package com.example.oauthjwt2.service.oauth;

import com.example.oauthjwt2.CookieUtil;
import com.example.oauthjwt2.dto.AccessDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OAuth2 리다이렉트 문제로 access 토큰을 httpOnly 쿠키로 발급
 * -> 프론트에서 바로 재요청하면 해당 access 토큰 헤더에 싣고, 쿠키는 만료시킴
 */
@Slf4j
@Service
@Transactional
public class OAuth2JWTHeaderService {

    public AccessDTO oauth2JwtHeaderSet(HttpServletRequest request, HttpServletResponse response) {

        log.info("oauth2JwtHeaderSet을 실행합니다");
        //쿠키를 가져온다
        Cookie[] cookies = request.getCookies();
        String access = null;

        //쿠키에 대한 accessToken의 검증 처리
        if(cookies == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.info("bad");
            return null;
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("access")){
                access = cookie.getValue();
            }
        }

        if(access == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.info("bad");
            return null;
        }

        // 클라이언트의 access 토큰 쿠키를 만료, 그리고 추출된 토큰은 헤더에 담아 다시 보낸다
        response.addCookie(CookieUtil.createCookie("access", null, 0));
/*        response.addHeader("access", access);*/
        response.setStatus(HttpServletResponse.SC_OK);

        AccessDTO accessToken = AccessDTO.builder()
                .token(access)
                .build();

        return accessToken;
}
}