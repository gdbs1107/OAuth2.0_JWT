package com.example.oauthjwt2.service;

import com.example.oauthjwt2.dto.GoogleResponse;
import com.example.oauthjwt2.dto.NaverResponse;
import com.example.oauthjwt2.dto.OAuth2Response;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //OAuth2UserRequest 가 리소스 서버에서 전달하는 유저 정보를 다음 요청

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        //플랫폼을 구별 할 수 있는 Id
        String registrationId = userRequest.getClientRegistration().getRegistrationId();




        OAuth2Response oAuth2Response =null;

        if (registrationId.equals("naver")){

            oAuth2Response=new NaverResponse(oAuth2User.getAttributes());

        }else if (registrationId.equals("google")){

            oAuth2Response=new GoogleResponse(oAuth2User.getAttributes());


        }else {
            return null;
        }
    }
}
