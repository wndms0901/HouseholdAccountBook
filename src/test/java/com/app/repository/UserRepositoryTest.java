package com.app.repository;

import com.app.domain.user.Role;
import com.app.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

//    @AfterEach
//    public void cleanup(){
//        userRepository.deleteAll();
//    }

//    @Test
//    public void 회원등록_불러오기(){
//        // given
//       // Role role = Role.builder().roleName(RoleName.USER).build();
//
//        String name = "이름";
//        String email = "test123@test.com";
//        String password = "123";
//        User user = User.builder()
//                .name(name)
//                .email(email)
//                .password(password)
//                .monthStartDate("1")
//                .role(Role.USER)
//                .build();
//
//        //role.setUser(user);
//        userRepository.save(user);
//
//        // when
//        List<User> userList = userRepository.findAll();
//        int index = 0;
//        for(int i=0; i<userList.size(); i++){
//            if(userList.get(i).getEmail().equals(email)){
//                index=i;
//            }
//        }
//        // then
//        User result = userList.get(index);
//        assertThat(result.getEmail()).isEqualTo(email);
//        assertThat(result.getName()).isEqualTo(name);
//        assertThat(result.getMonthStartDate()).isEqualTo("1");
//    }

}
