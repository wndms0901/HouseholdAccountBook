package com.app.service;

import com.app.domain.user.User;
import com.app.dto.EmailDto;
import com.app.dto.UserDto;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;

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
    /**
     * 비밀번호 변경
     * @param userDto
     * @return UserDto
     */
    @Transactional
    @Override
    public UserDto updatePassword(User user, UserDto userDto){
        String email = user.updatePassword(passwordEncoder, userDto.getNewPassword()).getEmail();
        return UserDto.builder()
                .email(email)
                .build();
    }
    /**
     * 임시 비밀번호 이메일 전송
     * @param email
     * @return EmailDto
     */
    @Transactional
    @Override
    public void sendPasswordResetEmail(String email) {
        // 회원 정보 조회
        User user = findByEmail(email);
        // 이메일 내용 설정
        UserDto userDto = UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
        EmailDto emailDto = setEmailContent(userDto);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(emailDto.getTitle());
        message.setText(emailDto.getMessage());
        // 메일발송
        mailSender.send(message);
        // 비밀번호 변경
        user.updatePassword(passwordEncoder, emailDto.getTempPassword());
    }
    /**
     * 이메일 내용 설정
     * @param userDto
     * @return EmailDto
     */
    @Override
    public EmailDto setEmailContent(UserDto userDto) {
        // 임시 비밀번호 생성
        String tempPassword = createTempPassword();
        EmailDto emailDto = new EmailDto();
        emailDto.setAddress(userDto.getEmail());
        emailDto.setTitle(userDto.getName()+"님의 가계부 임시비밀번호 안내 이메일 입니다.");
        emailDto.setMessage("안녕하세요. 가계부 임시비밀번호 안내 관련 이메일 입니다.\n" + "[" + userDto.getName() + "]" +"님의 임시 비밀번호는 "
                + tempPassword + " 입니다.\n"+"임시 비밀번호로 로그인 후 내정보에서 비밀번호를 변경해 주세요.");
        emailDto.setTempPassword(tempPassword);
        return emailDto;
    }
    /**
     * 임시 비밀번호 생성
     * @return String
     */
    private String createTempPassword() {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
}
