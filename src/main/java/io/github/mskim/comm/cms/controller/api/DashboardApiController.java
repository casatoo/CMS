package io.github.mskim.comm.cms.controller.api;

import io.github.mskim.comm.cms.api.ApiPaths;
import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.dto.DashboardStatsDTO;
import io.github.mskim.comm.cms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 대시보드 API Controller
 *
 * <p>관리자 대시보드 통계 데이터를 제공하는 REST API입니다.</p>
 * <p>ROLE_ADMIN 권한이 필요합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.DASHBOARD)
public class DashboardApiController {

    private final DashboardService dashboardService;

    /**
     * 대시보드 통계 조회 (관리자 전용)
     *
     * <p>승인 대기 현황, 출근 현황 등 대시보드에 필요한 통계를 조회합니다.</p>
     * <p>결과는 30분간 캐시됩니다.</p>
     *
     * @return ApiResponse<DashboardStatsDTO>
     */
    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        return ApiResponse.of(
            ApiStatus.OK,
            "대시보드 통계를 조회했습니다.",
            stats
        );
    }
}
