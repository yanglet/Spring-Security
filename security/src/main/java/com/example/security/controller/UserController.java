package com.example.security.controller;

import com.example.security.entity.User;
import com.example.security.entity.UserForm;
import com.example.security.repository.UserRepository;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/exex")
    public String error(){
        throw new IllegalStateException("오류입니다");
    }

    @GetMapping("/loginForm")
    public String loginform(){
        return "basic/loginForm";
    }

    @GetMapping("/join")
    public String joinForm(Model model){
        model.addAttribute("userForm", new UserForm());
        return "basic/join";
    }
    @PostMapping("/join")
    public String join(@Validated @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "basic/join";
        }else{
            User user = User.builder()
                    .username(userForm.getUsername())
                    .age(userForm.getAge())
                    .email(userForm.getEmail())
                    .password(userForm.getPassword())
                    .role(userForm.getRole())
                    .build();

            userService.join(user);

            return "redirect:/loginForm";
        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public String members(Model model){
        List<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        return "basic/users";
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/calendar")
    public String calendar(){
        return "basic/calendar";
    }
}
