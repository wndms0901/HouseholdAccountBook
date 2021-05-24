package com.app.service;

import com.app.domain.user.User;
import com.app.dto.EmailDto;
import com.app.dto.UserDto;

public interface UserService {
    /**
     * 회원 등록
     * @param user
     * @return User
     */
    User registerUser(User user);
    /**
     * 회원 조회
     * @param email
     * @return User
     */
    User findByEmail(String email);
    /**
     * 회원 비밀번호 체크
     * @param rawPassword
     * @param encodedPassword
     * @return boolean
     */
    boolean checkPassword(String rawPassword, String encodedPassword);
    /**
     * 월시작일 업데이트
     * @param userDto
     * @return void
     */
    void updateMonthStartDate(UserDto userDto);
    /**
     * 비밀번호 변경
     * @param userDto
     * @return UserDto
     */
    UserDto updatePassword(User user, UserDto userDto);
    /**
     * 임시 비밀번호 이메일 전송
     * @param email
     * @return EmailDto
     */
    void sendPasswordResetEmail(String email);
    /**
     * 이메일 내용 설정
     * @param userDto
     * @return EmailDto
     */
    EmailDto setEmailContent(UserDto userDto);
}
