package com.app.service;

import com.app.domain.user.User;
import com.app.dto.UserDto;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

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
    @Override
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
    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 월시작일 업데이트
     * @param userDto
     * @return void
     */
    @Transactional
    @Override
    public void updateMonthStartDate(UserDto userDto){
        Optional<User> optional = userRepository.findByEmail(userDto.getEmail());
        if(!optional.isPresent()) {
            throw new UsernameNotFoundException(userDto.getEmail() + " 사용자 없음");
        }else{
            optional.get().updateMonthStartDate(userDto.getMonthStartDate());

        }

    }
}
