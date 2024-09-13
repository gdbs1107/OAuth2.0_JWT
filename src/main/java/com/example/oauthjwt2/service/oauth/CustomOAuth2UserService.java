package com.example.oauthjwt2.service.oauth;

import com.example.oauthjwt2.dto.*;
import com.example.oauthjwt2.dto.response.GoogleResponse;
import com.example.oauthjwt2.dto.response.KakaoResponse;
import com.example.oauthjwt2.dto.response.NaverResponse;
import com.example.oauthjwt2.dto.response.OAuth2Response;
import com.example.oauthjwt2.entity.UserEntity;
import com.example.oauthjwt2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;


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


        }else if (registrationId.equals("kakao")){

            oAuth2Response=new KakaoResponse(oAuth2User.getAttributes());

        }
        else {
            return null;
        }



        //리소스 서버에서 넘기는 username은 겹칠 수 있기 때문에 우리만의 username을 설정해줌
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData=userRepository.findByUsername(username);

        if(existData==null){

            UserEntity newUser = UserEntity.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .password("tempPassword")
                    .role("ROLE_USER")
                    .build();

            userRepository.save(newUser);


            UserDTO userDTO = UserDTO.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(userDTO);

        }else {


            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            userRepository.save(existData);

            UserDTO userDTO = UserDTO.builder()
                    .username(existData.getUsername())
                    .name(oAuth2Response.getName())
                    .role(existData.getRole())
                    .build();

            return new CustomOAuth2User(userDTO);

        }

    }
}
