package io.github.mskim.comm.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String rootAccess() {
        return "redirect:/main";
    }
}
