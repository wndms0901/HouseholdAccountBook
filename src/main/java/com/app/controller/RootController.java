package com.app.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController implements ErrorController {
    @GetMapping("/error")
    public String redirectRoot(){
        return "index.html";
    }

    @Override
    public String getErrorPath() {
        // error가 발생하는 경우 index.html로 이동하도록 처리
        return "/error";
    }
}
