package com.example.security.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
public class User extends BaseEntity {
    @Id @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    private Integer age;
    private String role;

    protected User(){}
}
