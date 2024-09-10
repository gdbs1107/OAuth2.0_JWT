package com.example.oauthjwt2.dto.response;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    // 카카오 계정 정보를 가져오기 위한 헬퍼 메소드
    private Map<String, Object> getKakaoAccount() {
        return (Map<String, Object>) attribute.get("kakao_account");
    }

    // 카카오 프로필 정보를 가져오기 위한 헬퍼 메소드
    private Map<String, Object> getProfile() {
        return (Map<String, Object>) getKakaoAccount().get("profile");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = getKakaoAccount();
        return kakaoAccount.get("email") != null ? kakaoAccount.get("email").toString() : null;
    }

    @Override
    public String getName() {
        Map<String, Object> profile = getProfile();
        return profile.get("nickname") != null ? profile.get("nickname").toString() : null;
    }
}

