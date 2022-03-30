package com.example.security.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    @NotEmpty(message = "필수 입력란입니다.")
    private String username;
    @NotNull
    @Size(min = 6, max = 14, message = "비밀번호는 6자리 이상 14자리 이하여야 합니다")
    private String password;
    private String email;
    private Integer age;
    private String role;
}
