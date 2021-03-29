package com.app.service;

import com.app.domain.User;

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
}
