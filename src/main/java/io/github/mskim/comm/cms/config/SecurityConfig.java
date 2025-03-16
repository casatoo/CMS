package io.github.mskim.comm.cms.config;

import io.github.mskim.comm.cms.jwt.JWTFilter;
import io.github.mskim.comm.cms.jwt.JWTUtil;
import io.github.mskim.comm.cms.jwt.LoginFilter;
import io.github.mskim.comm.cms.service.UserLoginHistoryService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // AuthenticationManager 에서 사용할 Configuration 생성자 주입
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserLoginHistoryService userLoginHistoryService;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, CustomAuthenticationEntryPoint authenticationEntryPoint, UserLoginHistoryService userLoginHistoryService, JWTUtil jwtUtil) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationConfiguration = authenticationConfiguration;
        this.userLoginHistoryService = userLoginHistoryService;
        this.jwtUtil = jwtUtil;
    }
    // AuthenticationManager Bean 생성
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsProperties corsProperties) throws Exception{

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/join", "/loginProc", "/joinProc").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/fonts/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 세션 무효화 (JSESSIONID 삭제)
                            request.getSession().invalidate();
                            // "Authorization" 쿠키 삭제
                            Cookie cookie = new Cookie("Authorization", null);
                            cookie.setHttpOnly(true);
                            cookie.setSecure(true);
                            cookie.setPath("/");
                            cookie.setMaxAge(0); // 즉시 삭제
                            response.addCookie(cookie);

                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, userLoginHistoryService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource(corsProperties)));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsProperties corsProperties) {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(corsProperties.getAllowed()); // yml에서 가져온 값 사용
        configuration.setAllowedMethods(List.of("GET", "POST"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}