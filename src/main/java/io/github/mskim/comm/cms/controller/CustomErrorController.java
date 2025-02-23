package io.github.mskim.comm.cms.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public String handleError(HttpServletRequest request, Model model) {
        // 에러 정보 가져오기
        Object status = request.getAttribute("javax.servlet.error.status_code");
        Object message = request.getAttribute("javax.servlet.error.message");

        // 모델에 에러 정보 추가
        model.addAttribute("status", status);
        model.addAttribute("message", message);

        // error.html로 이동
        return "error"; // error.html을 반환
    }
}
