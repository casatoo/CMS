package io.github.mskim.comm.cms.controller.view;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 사용자 관리 View Controller
 *
 * <p>관리자 전용 사용자 관리 페이지를 제공합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Controller
@RequestMapping("/view")
public class UserViewController {

    /**
     * 사용자 관리 페이지 (관리자 전용)
     *
     * @param model 모델
     * @return 사용자 관리 페이지
     */
    @GetMapping("/user01")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String userManagementPage(Model model) {
        return "/user/user01";
    }
}
