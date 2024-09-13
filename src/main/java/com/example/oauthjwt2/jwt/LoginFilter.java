package com.example.oauthjwt2.jwt;

import com.example.oauthjwt2.CookieUtil;
import com.example.oauthjwt2.dto.UserDTO;
import com.example.oauthjwt2.entity.RefreshEntity;
import com.example.oauthjwt2.repository.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper 추가



    //login을 시작하는 메서드 -> UsernamePasswordAuthenticationFilter 에서 상속 받음
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("일반 로그인을 시작합니다");

        try {
            // JSON 요청 본문을 LoginRequestDto로 매핑
            UserDTO.LoginDTO loginRequestDto = objectMapper.readValue(request.getInputStream(), UserDTO.LoginDTO.class);

            String username = loginRequestDto.getUsername();
            String password = loginRequestDto.getPassword();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            // 인증 시도
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Authentication authentication에서 유저정보를 ㅏㄱ져옴
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws ServletException, IOException {


        // 소셜 로그인 관련 URL인지 확인
        if (request.getRequestURI().startsWith("/oauth2")) {
            // 소셜 로그인 요청인 경우 LoginFilter의 로직을 실행하지 않음
            log.info("소셜로그인으로 인식하고 다음 필터로 넘깁니다");
            chain.doFilter(request, response);
            return;
        }

        //유저 정보
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L);//10분
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);//24시간

        //응답 설정
        response.setHeader("Authorization", access);
        response.addCookie(CookieUtil.createCookie("refresh", refresh,86400000));

        addRefreshEntity(username,refresh,86400000L);

        response.setStatus(HttpStatus.OK.value());

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = RefreshEntity.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }

}
