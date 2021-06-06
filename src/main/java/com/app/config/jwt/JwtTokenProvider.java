package com.app.config.jwt;

import com.app.service.SecurityUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    // 토큰 유효시간 1시간
    private long tokenValidTime = 60 * 60 * 1000L;

    private final SecurityUserDetailsService securityUserDetailsService;

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // 객체 초기화, secretKey를 Base64로 인코딩
   // @PostConstruct
    //protected void init() {
    //    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
   // }

    // JWT 토큰 생성
    public String createToken(String userId, String role) {
//    public String createToken(String userId, List<String> roles) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // String 형태의 키를 byte로 만들어줌
        byte[] secretKeybytes = DatatypeConverter.parseBase64Binary(secretKey);

        // 암호화 키를 만듬
        Key signingKey = new SecretKeySpec(secretKeybytes, signatureAlgorithm.getJcaName());

        // claim : JWT payload 에 저장되는 정보단위
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", role); // 정보는 key / value 쌍으로 저장
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(signingKey)  // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = securityUserDetailsService.loadUserByUsername(this.getUserId(token));
        for(GrantedAuthority i : userDetails.getAuthorities()){
            System.out.println(">>>>"+i.getAuthority());
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserId(String token) {
        return getClaimsFromJwtToken(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옴. "Authorization" : "TOKEN값'
    public String resolveJwtToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalStateException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
//        try {
//            Jws<Claims> claims = getClaimsFromJwtToken(token);
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
    }
    private Jws<Claims> getClaimsFromJwtToken(String token) throws JwtException{
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .build()
                .parseClaimsJws(token);
    }
}