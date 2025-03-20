package io.github.mskim.comm.cms.controller.view;

import io.github.mskim.comm.cms.api.ApiPaths;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AttendanceViewController {

    @RequestMapping(ApiPaths.ATTENDANCE + "01")
    public String att01 (Model model) {
        return "/attendance/att01";
    }

    @RequestMapping(ApiPaths.ATTENDANCE + "02")
    public String att02 (Model model) {
        return "/attendance/att02";
    }

    @RequestMapping(ApiPaths.ATTENDANCE + "03")
    public String att03 (Model model) {
        return "/attendance/att03";
    }

}
