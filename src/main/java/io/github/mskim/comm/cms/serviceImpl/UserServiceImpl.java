package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.exception.BusinessException;
import io.github.mskim.comm.cms.exception.ErrorCode;
import io.github.mskim.comm.cms.repository.UserRepository;
import io.github.mskim.comm.cms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 서비스 구현
 *
 * <p>사용자 정보 조회 및 관리를 담당합니다.</p>
 * <p>자주 조회되는 사용자 정보는 캐시를 통해 성능을 향상시킵니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * 로그인 ID로 사용자 조회
     *
     * <p>조회 결과는 30분간 캐시됩니다.</p>
     * <p>캐시 키: "users::{loginId}"</p>
     *
     * @param loginId 로그인 ID
     * @return Users 엔티티
     * @throws BusinessException 사용자를 찾을 수 없는 경우
     */
    @Override
    @Cacheable(value = "users", key = "#loginId")
    public Users findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new BusinessException(
                ErrorCode.USER_NOT_FOUND,
                "사용자를 찾을 수 없습니다: " + loginId
            ));
    }
}
