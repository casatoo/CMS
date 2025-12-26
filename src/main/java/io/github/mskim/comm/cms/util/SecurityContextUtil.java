package io.github.mskim.comm.cms.util;

import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * SecurityContext 접근 유틸리티
 *
 * <p>현재 로그인한 사용자 정보를 조회하는 공통 로직을 제공합니다.</p>
 * <p>Service 계층에서 반복되는 SecurityContext 접근 패턴을 제거합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class SecurityContextUtil {

    private final UserRepository userRepository;

    /**
     * 현재 인증된 사용자의 로그인 ID 조회
     *
     * @return 로그인 ID (username)
     * @throws IllegalStateException 인증되지 않은 경우
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다");
        }

        return authentication.getName();
    }

    /**
     * 현재 인증된 사용자 엔티티 조회
     *
     * @return Users 엔티티
     * @throws IllegalStateException 인증되지 않은 경우
     * @throws RuntimeException 사용자를 찾을 수 없는 경우
     */
    public Users getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByLoginId(username)
            .orElseThrow(() -> new RuntimeException(
                "사용자를 찾을 수 없습니다: " + username
            ));
    }

    /**
     * 현재 사용자의 ID 조회
     *
     * @return 사용자 ID
     */
    public String getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * 현재 사용자의 이름 조회
     *
     * @return 사용자 이름
     */
    public String getCurrentUserName() {
        return getCurrentUser().getName();
    }

    /**
     * 인증 여부 확인
     *
     * @return 인증되었으면 true, 아니면 false
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

        return authentication != null && authentication.isAuthenticated();
    }
}
