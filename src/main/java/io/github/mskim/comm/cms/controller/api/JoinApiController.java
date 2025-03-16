package io.github.mskim.comm.cms.controller.api;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.JoinDTO;
import io.github.mskim.comm.cms.service.JoinService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JoinApiController {

    private final JoinService joinService;

    public JoinApiController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/joinProc")
    public ApiResponse joinProcess(@RequestBody JoinDTO joinDTO) {
        return joinService.joinProcess(joinDTO);
    }
}
