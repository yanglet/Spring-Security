package com.example.security.security.oauth2;

import com.example.security.entity.User;
import com.example.security.security.auth.PrincipalDetails;
import com.example.security.security.oauth2.provider.GoogleUserInfo;
import com.example.security.security.oauth2.provider.NaverUserInfo;
import com.example.security.security.oauth2.provider.OAuth2UserInfo;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        }
        else{
            System.out.println("지원하지 않는 소셜 로그인 입니다.");
        }

        User userEntity = userService.oauthJoin(oAuth2UserInfo);

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
