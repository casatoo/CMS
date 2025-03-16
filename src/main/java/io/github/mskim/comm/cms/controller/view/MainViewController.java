package io.github.mskim.comm.cms.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainViewController {

    @RequestMapping("/")
    public String root (Model model) {
        return "redirect:/main";
    }

    @RequestMapping("/main")
    public String mainPage (Model model) {
        return "/main/mainView";
    }
}
