package io.github.mskim.comm.cms.config;

import io.github.mskim.comm.cms.dto.UserDTO;
import io.github.mskim.comm.cms.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void commonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String loginId = auth.getName();
            UserDTO user = userService.findByLoginId(loginId);
            model.addAttribute("loginUser", user.getName());
            model.addAttribute("rank", user.getRank());
        }
    }
}
