package com.example.security.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private Integer age;
    private String role;

    // 어떤 방식으로 로그인했는지 ex) 구글 등 -> userRequest.getClientRegistration()
    private String provider;
    private String providerId;

    protected User(){}
}
