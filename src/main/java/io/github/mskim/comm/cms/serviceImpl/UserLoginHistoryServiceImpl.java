package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.config.CustomModelMapper;
import io.github.mskim.comm.cms.dto.UserLoginHistoryDTO;
import io.github.mskim.comm.cms.entity.UserLoginHistory;
import io.github.mskim.comm.cms.repository.UserLoginHistoryRepository;
import io.github.mskim.comm.cms.service.UserLoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 로그인 이력 서비스 구현
 *
 * <p>MyBatis Mapper 의존성을 제거하고 JPA Repository로 완전 마이그레이션</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserLoginHistoryServiceImpl implements UserLoginHistoryService {

    private final UserLoginHistoryRepository userLoginHistoryRepository;

    @Override
    @Transactional
    public void save(UserLoginHistory userLoginHistory) {
        userLoginHistoryRepository.save(userLoginHistory);
    }

    @Override
    public List<UserLoginHistoryDTO> findByUserId(String userId) {
        // JPA Repository 사용 (MyBatis 제거)
        List<UserLoginHistory> userLoginHistory = userLoginHistoryRepository.findByUserId(userId);

        return userLoginHistory != null && !userLoginHistory.isEmpty()
            ? CustomModelMapper.mapList(userLoginHistory, UserLoginHistoryDTO.class)
            : List.of();
    }
}
