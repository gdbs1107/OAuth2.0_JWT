package com.example.oauthjwt2.service;

import com.example.oauthjwt2.CookieUtil;
import com.example.oauthjwt2.jwt.JWTUtil;
import com.example.oauthjwt2.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/*액세스 토큰 만료시, 리프레시 토큰 검증을 통해
        액세스를 재발급해주는 서비스 코드*/
@RequiredArgsConstructor
@Service
public class ReissueService {


    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final RefreshTokenService refreshTokenService;


    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        //쿠키에서 리프레시 토큰을 받고
        refresh = Arrays.stream(cookies).filter((cookie) -> cookie.getName().equals("refresh"))
                .findFirst().get().getValue();

        // 쿠키에 refresh 토큰 x
        if (refresh == null) {
            return new ResponseEntity<>("refresh token is null", HttpStatus.BAD_REQUEST);
        }

        // 만료된 토큰은 payload 읽을 수 없음 -> ExpiredJwtException 발생
        try {
            jwtUtil.isExpired(refresh);
        } catch(ExpiredJwtException e){
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // refresh 토큰이 아님
        String category = jwtUtil.getCategory(refresh);
        if(!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // refresh DB 조회
        Boolean isExist = refreshRepository.existsByRefresh(refresh);

        // DB 에 없는 리프레시 토큰 (혹은 블랙리스트 처리된 리프레시 토큰)
        if(!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        /*모든 검증을 통과했다면 새로운 access발급
        여기서 refresh도 새로 발급해주는데 이는 리프레시 토큰 탈취에 대비한 Refresh Rotate를 구현한 것*/
        String newAccess = jwtUtil.createJwt("access", username, role, 60 * 10 * 1000L);
        Integer expiredS = 60 * 60 * 24;
        String newRefresh = jwtUtil.createJwt("refresh", username, role, expiredS * 1000L);

        // 기존 refresh DB 삭제, 새로운 refresh 저장
        refreshRepository.deleteByRefresh(refresh);
        refreshTokenService.saveRefresh(username, expiredS, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(CookieUtil.createCookie("refresh", newRefresh, expiredS));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
