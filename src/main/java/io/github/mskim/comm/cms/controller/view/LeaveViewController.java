package io.github.mskim.comm.cms.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class LeaveViewController {

    @GetMapping("/attendance02")
    public String attendance02(Model model) {
        return "/attendance/att02";
    }

    @GetMapping("/attendance03")
    public String attendance03(Model model) {
        return "/attendance/att03";
    }
}
