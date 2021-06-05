package com.app.controller;

import com.app.config.jwt.JwtTokenProvider;
import com.app.domain.user.Role;
import com.app.dto.UserDto;
import com.app.domain.user.User;
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

    /**
     * 로그인
     * @param userDto
     * @return ResponseEntity<?>
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 회원조회
        User user = userService.findByEmail(userDto.getEmail());
        if(user!=null) {
            // 비밀번호 체크
            boolean checkPw = userService.checkPassword(userDto.getPassword(), user.getPassword());
            if (!checkPw) {
                resultMap.put("error", "login failed");
            } else { // 토큰 생성
                String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getKey());
                resultMap.put("token", token);
                // 회원 정보
                UserDto userInfo = UserDto.builder().email(user.getEmail()).name(user.getName()).monthStartDate(user.getMonthStartDate()).build();
                resultMap.put("userInfo", userInfo);
            }
        }else{
            // 회원x
            resultMap.put("error", "login failed");
        }
         return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    /**
     * 테스트 계정 로그인
     * @return ResponseEntity<?>
     */
    @GetMapping("login/test-id")
    public ResponseEntity<?> testIdLogin() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String email = "test@test.com";
        // 회원조회
        User user = userService.findByEmail(email);
        // 토큰 생성
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getKey());
        resultMap.put("token", token);
        // 회원 정보
        UserDto userInfo = UserDto.builder().email(user.getEmail()).name(user.getName()).monthStartDate(user.getMonthStartDate()).build();
        resultMap.put("userInfo", userInfo);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    /**
     * 회원등록
     * @param userDto
     * @return ResponseEntity<?>
     */
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
//        Role role = Role.builder().roleName(RoleName.USER).build();
        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .role(Role.USER)
                .build();
//        role.setUser(user);
        userService.registerUser(user);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    /**
     * 회원등록 확인
     * @param email
     * @return ResponseEntity<?>
     */
    @GetMapping("register/check")
    public ResponseEntity<?> selectRegisterCheck(@RequestParam String email) {
        User user = userService.findByEmail(email);
        String userEmail = user != null? user.getEmail() : "";
        return new ResponseEntity(UserDto.builder().email(userEmail).build(), HttpStatus.OK);
    }
    /**
     * 임시 비밀번호 이메일 전송
     * @param email
     * @return ResponseEntity<?>
     */
    @GetMapping("send/password-reset")
    public ResponseEntity<?> sendPasswordResetEmail(@RequestParam String email) {
        userService.sendPasswordResetEmail(email);
        return new ResponseEntity("success", HttpStatus.OK);
    }

    /**
     * 월시작일 업데이트
     * @param userDto
     * @return ResponseEntity<?>
     */
    @PostMapping("update/month-start-date")
    public ResponseEntity<?> updateMonthStartDate(@RequestBody UserDto userDto) {
        userService.updateMonthStartDate(userDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 비밀번호 변경
     * @param userDto
     * @return ResponseEntity<?>
     */
    @PostMapping("update/password")
    public ResponseEntity<?> updatePassword(@RequestBody UserDto userDto) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 회원조회
        User user = userService.findByEmail(userDto.getEmail());
        // 비밀번호 체크
        boolean checkPw = userService.checkPassword(userDto.getPassword(), user.getPassword());
        if(!checkPw){ // 비밀번호 일치x
            return new ResponseEntity<>(null, HttpStatus.OK);
        }else{ // 비밀번호 일치o
            return new ResponseEntity<>(userService.updatePassword(user, userDto), HttpStatus.OK);
        }
    }
}
