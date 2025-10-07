package io.github.mskim.comm.cms.controller.api;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.JoinDTO;
import io.github.mskim.comm.cms.service.JoinService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@Validated
public class JoinApiController {

    private final JoinService joinService;

    public JoinApiController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/joinProc")
    public ApiResponse joinProcess(@Valid @RequestBody JoinDTO joinDTO) {
        return joinService.joinProcess(joinDTO);
    }
}
