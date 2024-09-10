package com.example.oauthjwt2.dto.response;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private Map<String, Object> attributes; // getAttributes
    private Map<String, Object> attributesProperties; // getAttributes
    private Map<String, Object> attributesAccount; // getAttributes

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.attributesProperties = (Map<String, Object>) attributes.get("properties");
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) attributesAccount.get("email");
    }

    @Override
    public String getName() {
        return (String) attributesProperties.get("nickname");
    }
}
