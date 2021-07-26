package com.app.config.security;

import com.app.config.jwt.JwtErrorCode;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 권한이 없을 경우. 401 error
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String)request.getAttribute("exception");

        /**
         * 토큰 없는 경우
         */
        if(exception == null) {
            setResponse(response, JwtErrorCode.NULL_TOKEN);
            return;
        }

        /**
         * 토큰 만료된 경우
         */
        if(exception.equals(JwtErrorCode.EXPIRED_TOKEN.getCode())) {
            setResponse(response, JwtErrorCode.EXPIRED_TOKEN);
        }

        /**
         * 토큰 시그니처가 다른 경우
         */
        if(exception.equals(JwtErrorCode.INVALID_TOKEN.getCode())) {
            setResponse(response, JwtErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * 한글 출력을 위해 getWriter() 사용
     */
    private void setResponse(HttpServletResponse response, JwtErrorCode jwtErrorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"message\" : \"" + jwtErrorCode.getMessage()
                + "\", \"code\" : \"" +  jwtErrorCode.getCode()
                + "\", \"status\" : " + jwtErrorCode.getStatus()
                + " }");
    }
}
