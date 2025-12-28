package io.github.mskim.comm.cms.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 관리자 대시보드 View Controller
 *
 * <p>관리자 대시보드 페이지를 렌더링합니다.</p>
 * <p>ROLE_ADMIN 권한이 필요합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class DashboardViewController {

    /**
     * 관리자 대시보드 페이지 (관리자 전용)
     *
     * <p>전체 시스템 현황을 한눈에 볼 수 있는 대시보드를 제공합니다.</p>
     *
     * @return 대시보드 뷰 템플릿 경로
     */
    @RequestMapping("/view/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String dashboardPage() {
        return "/dashboard/dashboardView";
    }
}
