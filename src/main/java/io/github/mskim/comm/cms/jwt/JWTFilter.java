package io.github.mskim.comm.cms.jwt;

import io.github.mskim.comm.cms.dto.CustomUserDetails;
import io.github.mskim.comm.cms.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 특정 경로 또는 패턴을 제외
        return path.equals("/loginProc") ||
                path.equals("/joinProc") ||
                path.equals("/login") ||
                path.equals("/join") ||
                path.equals("/") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/img/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키에서 Authorization 찾기
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Authorization 헤더가 없고 쿠키도 없는 경우
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // UserEntity를 생성하여 값 set
        UserEntity user = new UserEntity();
        user.setLoginId(username);
        user.setPassword(""); // 임시 비밀번호 (보안)
        user.setRole(role);

        // UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 새로운 토큰 발행
        String newToken = jwtUtil.createJwt(customUserDetails.getUsername(), customUserDetails.getUserEntity().getRole(), 60*60*10L);

        // JWT를 쿠키에 저장
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setHttpOnly(true); // 클라이언트 측 JavaScript에서 접근 불가
        cookie.setSecure(true);  // HTTPS 환경에서만 작동 (보안 강화)
        cookie.setPath("/"); // 모든 경로에서 유효
        cookie.setMaxAge(60 * 60); // 쿠키 유효 시간 (초)
        response.addCookie(cookie);

        filterChain.doFilter(request, response);
    }
}
