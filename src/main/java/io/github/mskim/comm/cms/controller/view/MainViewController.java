package io.github.mskim.comm.cms.controller.view;

import io.github.mskim.comm.cms.dto.UserAttendanceDTO;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import io.github.mskim.comm.cms.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class MainViewController {

    private final UserService userService;

    private final UserAttendanceService userAttendanceService;

    public MainViewController(UserService userService, UserAttendanceService userAttendanceService) {
        this.userAttendanceService = userAttendanceService;
        this.userService = userService;
    }

    @RequestMapping("/")
    public String root (Model model) {
        return "redirect:/main";
    }

    @RequestMapping("/main")
    public String mainPage (Model model, HttpServletResponse response) {
        // 브라우저 캐시 방지 헤더 설정
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        LocalDateTime now = LocalDateTime.now();

        // 연차
        Users user = userService.findByLoginId(loginId);
        model.addAttribute("annualLeaveDays", user.getAnnualLeaveDays() + "일");

        // 출근시간
        UserAttendanceDTO userAttendanceDTO = userAttendanceService.findTodayCheckInTime(user.getId());
        String checkInTime;
        String checkOutTime;
        if (ObjectUtils.isEmpty(userAttendanceDTO)) {
            checkInTime = "- - : - - : - -";
            checkOutTime = "- - : - - : - -";
            model.addAttribute("checkInExists", false);
            model.addAttribute("checkOutAvailable", false);
        } else {
            LocalDateTime checkInDateTime = userAttendanceDTO.getCheckInTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            checkInTime = checkInDateTime.format(formatter);
            if (ObjectUtils.isEmpty(userAttendanceDTO.getCheckOutTime())) {
                checkOutTime = "- - : - - : - -";
                model.addAttribute("checkOutExists", false);
            } else {
                LocalDateTime checkOutDateTime = userAttendanceDTO.getCheckOutTime();
                checkOutTime = checkOutDateTime.format(formatter);
                model.addAttribute("checkOutExists", true);
            }
            model.addAttribute("checkInExists", true);
            LocalDateTime workEndTime = userAttendanceDTO.getCheckInTime().plusHours(8);
            if (now.isAfter(workEndTime)) {
                model.addAttribute("checkOutAvailable", true);
            } else {
                model.addAttribute("checkOutAvailable", false);
            }
        }
        model.addAttribute("checkInTime", checkInTime);
        model.addAttribute("checkOutTime", checkOutTime);
        // 근무일수
        int workDayCount = userAttendanceService.countWorkDaysThisMonth(user.getId());
        model.addAttribute("workDayCount", workDayCount + "일");

        return "/main/mainView";
    }

    /**
     * 출퇴근 기록 페이지
     */
    @RequestMapping("/view/history01")
    public String attendanceHistoryPage() {
        return "/main/history01";
    }
}
