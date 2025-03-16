package io.github.mskim.comm.cms.controller.view;

import io.github.mskim.comm.cms.api.ApiPaths;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AttendanceViewController {

    @RequestMapping(ApiPaths.ATTENDANCE)
    public String attendancePage (Model model) {
        return "/attendance/attendance";
    }

}
