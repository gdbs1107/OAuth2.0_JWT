package com.example.oauthjwt2.dto.response;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final String providerId;
    private final String name;
    private final String email;

    public KakaoResponse(Map<String, Object> attributes) {
        // 'id'는 카카오 응답의 최상위 요소에서 가져옵니다.
        this.providerId = String.valueOf(attributes.get("id"));

        // 'properties'와 'kakao_account'에서 사용자 정보를 가져옵니다.
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        this.name = (String) properties.get("nickname");
        this.email = (String) kakaoAccount.get("email");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
