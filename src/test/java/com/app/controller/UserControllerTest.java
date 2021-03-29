package com.app.controller;

import com.app.config.jwt.JwtTokenProvider;
import com.app.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void loginTokenTest() throws Exception {
        // given
        UserDto userResponseDto = UserDto.builder().email("test@test.com1").password("test123!").build();
        String url = "http://localhost:" + port + "/user/login";

        // when
        ResponseEntity<?> responseEntity = testRestTemplate.postForEntity(url, userResponseDto, Object.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

}
