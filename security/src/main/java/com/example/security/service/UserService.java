package com.example.security.service;

import com.example.security.entity.User;
import com.example.security.repository.UserRepository;
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

    private boolean validateDuplicateMember(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
        return false;
    }
}
