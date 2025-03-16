package io.github.mskim.comm.cms.controller.view;

import io.github.mskim.comm.cms.dto.UserAttendanceDTO;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import io.github.mskim.comm.cms.service.UserService;
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
    public String mainPage (Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        LocalDateTime now = LocalDateTime.now();

        // 연차
        Users user = userService.findByLoginId(loginId);
        model.addAttribute("annualLeaveDays", user.getAnnualLeaveDays() + "일");

        // 출근시간
        UserAttendanceDTO userAttendanceDTO = userAttendanceService.findTodayCheckInTime(user.getId());
        String checkInTime;
        if (ObjectUtils.isEmpty(userAttendanceDTO)) {
            checkInTime = "- - : - - : - -";
            model.addAttribute("checkInExists", false);
            model.addAttribute("checkOutAvailable", false);
        } else {
            LocalDateTime checkInDateTime = userAttendanceDTO.getCheckInTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            checkInTime = checkInDateTime.format(formatter);
            model.addAttribute("checkInExists", true);
            LocalDateTime workEndTime = userAttendanceDTO.getCheckInTime().plusHours(8);
            if (now.isAfter(workEndTime)) {
                model.addAttribute("checkOutAvailable", true);
            } else {
                model.addAttribute("checkOutAvailable", false);
            }

        }
        model.addAttribute("checkInTime", checkInTime);

        // 근무일수
        int workDayCount = userAttendanceService.countWorkDaysThisMonth(user.getId());
        model.addAttribute("workDayCount", workDayCount + "일");

        return "/main/mainView";
    }
}
