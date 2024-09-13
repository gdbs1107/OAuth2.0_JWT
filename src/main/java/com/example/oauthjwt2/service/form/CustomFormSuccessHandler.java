package com.example.oauthjwt2.service.form;

import com.example.oauthjwt2.CookieUtil;
import com.example.oauthjwt2.jwt.JWTUtil;
import com.example.oauthjwt2.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CustomFormSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    /*기존에는 LoginFilter를 따로 정의해서 구현하였지만
    이번에는 로그인은 시큐리티에게 위임하고 successHandler만 만든다*/


    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // create JWT
        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // access
        String access = jwtUtil.createJwt("access", username, role, 60 * 10 * 1000L);
        response.setHeader("access", access);

        // refresh
        Integer expireS = 24 * 60 * 60;
        String refresh = jwtUtil.createJwt("refresh", username, role, expireS * 1000L);
        response.addCookie(CookieUtil.createCookie("refresh", refresh, expireS));

        // refresh 토큰 DB 저장
        refreshTokenService.saveRefresh(username, expireS, refresh);

        // json 을 ObjectMapper 로 직렬화하여 전달
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("name", username);

        new ObjectMapper().writeValue(response.getWriter(), responseData);

    }
}