package com.example.security.service;

import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
import com.example.security.security.oauth2.provider.OAuth2UserInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.security.entity.QUser.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public List<User> searchUser(String usernameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();

        if (usernameCond != null) {
            builder.and(user.username.eq(usernameCond));
        }
        if (ageCond != null) {
            builder.and(user.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(user)
                .where(builder)
                .fetch();
    }

    public void join(User user){
        String joinPw = user.getPassword();
        String encodePw = bCryptPasswordEncoder.encode(joinPw);
        user.setPassword(encodePw);

        if( !validateDuplicateMember(user) ) {
            userRepository.save(user);
        }
    }

    public User oauthJoin(OAuth2UserInfo oAuth2UserInfo){
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId; // google_12312894120840 이런식
        String password = bCryptPasswordEncoder.encode("양글렛");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(userEntity);
        }

        return userEntity;
    }

    private boolean validateDuplicateMember(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
        return false;
    }
}
