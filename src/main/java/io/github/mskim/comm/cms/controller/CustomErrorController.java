package io.github.mskim.comm.cms.controller;

import io.github.mskim.comm.cms.api.ApiPaths;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(ApiPaths.ERROR_PATH)
    public String handleError(HttpServletRequest request, Model model) {
        // 에러 정보 가져오기
        Object status = request.getAttribute("javax.servlet.error.status_code");
        Object message = request.getAttribute("javax.servlet.error.message");

        model.addAttribute("status", status);
        model.addAttribute("message", message);

        return "error";
    }

    // 매핑되지 않은 URL을 처리하기 위한 핸들러
    @GetMapping(ApiPaths.VIEW_API + "/{path}")
    public String handleUnknownPath(@PathVariable(name = "path") String path, Model model) {

        // 에러 메시지와 상태 코드를 모델에 추가
        model.addAttribute("status", path);
        model.addAttribute("message", "페이지가 존재하지 않습니다.");

        return "error";
    }
}
