package com.app.controller;

import com.app.config.jwt.JwtTokenProvider;
import com.app.domain.Role;
import com.app.domain.RoleName;
import com.app.dto.UserDto;
import com.app.domain.User;
import com.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/user")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    // 로그인
     @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
         Map<String, Object> resultMap = new HashMap<String, Object>();
             User user = userService.findByEmail(userDto.getEmail());
            // 비밀번호 체크
             boolean checkPw = userService.checkPassword(userDto.getPassword(), user.getPassword());
             if(!checkPw){
                 resultMap.put("error","login failed");
             }else{ // 토큰 생성
                 String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoleList());
                 resultMap.put("token", token);
                 // 회원 정보
                 UserDto userInfo = UserDto.builder().email(user.getEmail()).name(user.getName()).monthStartDate(user.getMonthStartDate()).build();
                 resultMap.put("userInfo", userInfo);
             }

         return new ResponseEntity(resultMap, HttpStatus.OK);
                //return new ResponseEntity(HttpStatus.OK);
//        User user = securityUserDetailsService.loadUserByUsername(user.get("email"))
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
//        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
               }
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        Role role = Role.builder().roleName(RoleName.USER).build();
        User user = User.builder().email(userDto.getEmail()).name(userDto.getName()).password(userDto.getPassword()).build();
        role.setUser(user);
        userService.registerUser(user);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
