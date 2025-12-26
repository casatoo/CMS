package io.github.mskim.comm.cms.config;

import io.github.mskim.comm.cms.dto.UserDTO;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.thymeleaf.util.StringUtils;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void commonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !StringUtils.equals("anonymousUser", auth.getPrincipal())) {
            String loginId = auth.getName();
            Users user = userService.findByLoginId(loginId);
            model.addAttribute("loginUser", user.getName());
            model.addAttribute("position", user.getPosition());
        }
    }
}
