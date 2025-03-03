package io.github.mskim.comm.cms.controller;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.dto.JoinDTO;
import io.github.mskim.comm.cms.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class JoinController {

    private final JoinService joinService;

    // 생성자 주입 방식
    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @ResponseBody
    @PostMapping("/joinProc")
    public ApiResponse joinProcess(@RequestBody JoinDTO joinDTO) {
        return joinService.joinProcess(joinDTO);
    }
}
