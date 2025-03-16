package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.entity.UserLoginHistory;
import io.github.mskim.comm.cms.mapper.UserLoginHistoryMapper;
import io.github.mskim.comm.cms.repository.UserLoginHistoryRepository;
import io.github.mskim.comm.cms.service.UserLoginHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLoginHistoryServiceImpl implements UserLoginHistoryService {

    private final UserLoginHistoryRepository userLoginHistoryRepository;
    private final UserLoginHistoryMapper userLoginHistoryMapper;

    public UserLoginHistoryServiceImpl(UserLoginHistoryRepository userLoginHistoryRepository, UserLoginHistoryMapper userLoginHistoryMapper) {
        this.userLoginHistoryRepository = userLoginHistoryRepository;
        this.userLoginHistoryMapper = userLoginHistoryMapper;
    }

    @Override
    public void save(UserLoginHistory userLoginHistory) {
        userLoginHistoryRepository.save(userLoginHistory);
    }

    @Override
    public List<UserLoginHistory> findByUserId(String userId) {
        return userLoginHistoryMapper.findByUserId(userId);
    }
}
