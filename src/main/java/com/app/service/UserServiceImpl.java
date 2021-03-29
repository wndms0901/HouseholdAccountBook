package com.app.service;

import com.app.domain.User;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 회원 등록
     * @param user
     * @return User
     */
    @Override
    public User registerUser(User user) {
        user.encodePassword(passwordEncoder);
        return userRepository.save(user);
    }
    /**
     * 회원 조회
     * @param email
     * @return User
     */
    public User findByEmail(String email){
        Optional<User> optional = userRepository.findByEmail(email);
        if(!optional.isPresent()) {
            throw new UsernameNotFoundException(email + " 사용자 없음");
        }
        return optional.get();
    }
    /**
     * 회원 비밀번호 체크
     * @param rawPassword
     * @param encodedPassword
     * @return boolean
     */
    public boolean checkPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
