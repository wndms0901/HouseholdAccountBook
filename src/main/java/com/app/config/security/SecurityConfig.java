package com.app.config.security;

import com.app.config.jwt.JwtAuthenticationFilter;
import com.app.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    // 암호화에 필요한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // authenticationManager를 Bean 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // REST API만을 고려, 기본 설정 해제, HTTP 기본인증 비활성화
                .csrf().disable() // csrf 보안 토큰 disable처리.
                .formLogin().disable() // 폼 기반 인증 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용 X
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .antMatchers("/api/write/**", "/api/report/**",  "/api/budget/**",  "/api/excel/**"
                , "/api/user/update/**").authenticated()
                .anyRequest().permitAll()
                .and()
                // UsernamePasswordAuthenticationFilter 이전에 JwtAuthenticationFilter에서 인증 및 권한처리
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                // 인증 오류 발생 시 처리를 위한 핸들러 추가
                .exceptionHandling()
                // 인증 실패시 처리
                .authenticationEntryPoint(authenticationEntryPoint);
    }
}
