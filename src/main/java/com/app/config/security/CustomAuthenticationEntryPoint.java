package com.app.config.security;

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
        PrintWriter writer = response.getWriter();
        JSONObject data = new JSONObject();
        data.put("error","login failed");
        String jsonInfo = data.toJSONString();
        writer.print(jsonInfo);
       // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
    }
}
