package io.github.mskim.comm.cms.controller.api;

import io.github.mskim.comm.cms.api.ApiPaths;
import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.entity.UserLoginHistory;
import io.github.mskim.comm.cms.service.UserLoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.LOGIN_HISTORY)
public class UserLoginHistoryApiController {

    private final UserLoginHistoryService userLoginHistoryService;

    @GetMapping("/user-id")
    public ApiResponse<List<UserLoginHistory>> findByUserId(@RequestParam(name = "userId") String userId) {
        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryService.findByUserId(userId);
        if (userLoginHistoryList.size() == 0) {
            return ApiResponse.of(ApiStatus.OK, "로그인 이력이 없습니다.", userLoginHistoryList);
        } else {
            return ApiResponse.of(ApiStatus.OK, "로그인 이력 조회 성공", userLoginHistoryList);
        }
    }

}
