package io.github.mskim.comm.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/join", "/loginProc","/joinProc").permitAll() // 해당 경로는 모든 접근 가능
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // js, css, img 경로 접근 허용
                        .anyRequest().authenticated() // 나머지 모든 경로 - 모든 로그인 사용자 접근 가능
                );

        http
                .formLogin((auth) -> auth
                        .loginPage("/login") // 자동으로 로그인 페이지로 이동
                        .usernameParameter("loginId") // 커스터마이즈한 username 필드
                        .passwordParameter("password") // 기본 password 필드
                        .loginProcessingUrl("/loginProc") // 로그인 데이터를 넘겨 로그인처리를 진행
                        .defaultSuccessUrl("/main", true) // 로그인 성공 후 이동할 URL
                        .successHandler(new CustomAuthenticationSuccessHandler()) // 성공 핸들러 설정
                        .failureHandler(new CustomAuthenticationFailureHandler()) // 실패 핸들러 설정
                        .permitAll()
                );

        http    // csrf 설정 시 로그아웃 요청을 post 로 해야하지만 get 방식으로 요청 시 해당 설정이 필요 함, logoutController 설정도 필요
                .logout((auth) -> auth.logoutUrl("/logout")
                        .logoutSuccessUrl("/"));

        http    // Cross-Site Request Forgery
                .csrf((auth) -> auth.disable()); // 위변조 방지를 위한 설정 임시 disable 처리

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1) // 하나의 아이디에 대한 다중로그인 허용 갯수
                        .maxSessionsPreventsLogin(false)); // 다중로그인 갯수를 초과했을 때 처리 true : 새로운로그인 차단 , false : 기존 세션 하나 삭제

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation() // 세션 고정 공격을 방어하기 위한
                        //.none()); // 로그인 시 세션 정보 변경 안함
                        //.newSession()); // 로그인 시 세션 새로 생성
                        .changeSessionId()); // 로그인 시 동일 세션에 대한 id 변경

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // CORS 설정 추가

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 내부 요청만 허용 (로컬 및 내부 IP)
        configuration.setAllowedOrigins(List.of("http://localhost", "http://127.0.0.1", "http://192.168.1.0/24"));
        configuration.setAllowedMethods(List.of("GET", "POST"));
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 인증 정보 포함 가능

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}