package io.github.mskim.comm.cms.controller;

import io.github.mskim.comm.cms.dto.JoinDTO;
import io.github.mskim.comm.cms.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {

    private final JoinService joinService;

    // 생성자 주입 방식
    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinDTO joinDTO) {
        joinService.joinProcess(joinDTO);
        return "redirect:/login"; // /login URL로 리디렉션
    }
}
