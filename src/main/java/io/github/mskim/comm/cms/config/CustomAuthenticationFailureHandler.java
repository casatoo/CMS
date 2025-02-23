package io.github.mskim.comm.cms.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception instanceof BadCredentialsException) {
            // 사용자 이름이나 비밀번호가 틀린 경우
            errorMessage = "아이디 또는 비밀번호가 틀렸습니다.";
        } else {
            // 기타 에러 메시지
            errorMessage = "로그인 실패: " + exception.getMessage();
        }

        request.getSession().setAttribute("error", errorMessage); // 세션에 에러 메시지 저장
        // 에러 메시지를 URL 파라미터로 전달
        response.sendRedirect("/login?error=" + URLEncoder.encode(errorMessage, "UTF-8")); // URL 인코딩
    }
}