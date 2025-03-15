package io.github.mskim.comm.cms.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mskim.comm.cms.dto.CustomUserDetails;
import io.github.mskim.comm.cms.entity.UserLoginHistory;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.service.UserLoginHistoryService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserLoginHistoryService userLoginHistoryService; // UserLoginHistoryService 추가

    // 생성자에 UserLoginHistoryService 추가
    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserLoginHistoryService userLoginHistoryService) {
        setFilterProcessesUrl("/loginProc");
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userLoginHistoryService = userLoginHistoryService; // 주입받기
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> loginData = objectMapper.readValue(request.getInputStream(), Map.class);

            String loginId = loginData.get("loginId");
            String password = loginData.get("password");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password, null);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("로그인 요청 데이터를 읽을 수 없습니다.");
        }
    }

    // 로그인 성공 시 실행 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> aurhorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = aurhorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*10L);
        // JWT를 쿠키에 저장
        Cookie cookie = new Cookie("Authorization", token); // "Bearer " 제거
        cookie.setHttpOnly(true); // 클라이언트 측 JavaScript에서 접근 불가
        cookie.setSecure(true);  // HTTPS 환경에서만 작동 (보안 강화)
        cookie.setPath("/"); // 모든 경로에서 유효
        cookie.setMaxAge(60 * 60); // 쿠키 유효 시간 (초)
        response.addCookie(cookie);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 로그인 성공 시 로그인 이력 저장
        Users users = customUserDetails.getUserEntity();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        UserLoginHistory userLoginHistory = UserLoginHistory.builder()
                .user(users)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .loginTime(LocalDateTime.now())
                .build();

        userLoginHistoryService.save(userLoginHistory); // 로그인 이력 저장

        PrintWriter out = response.getWriter();
        out.write("{\"status\":200, \"message\":\"로그인 성공\"}");
        out.flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorMessage = "로그인에 실패하였습니다.";

        if (failed instanceof UsernameNotFoundException) {
            errorMessage = "존재하지 않는 아이디입니다.";
        } else if (failed instanceof BadCredentialsException) {
            errorMessage = "비밀번호가 올바르지 않습니다.";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"status\":401, \"message\":\"" + errorMessage + "\"}");
    }

}
