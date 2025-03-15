package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.entity.UserLoginHistory;
import io.github.mskim.comm.cms.repo.UserLoginHistoryRepository;
import io.github.mskim.comm.cms.service.UserLoginHistoryService;
import org.springframework.stereotype.Service;

@Service
public class UserLoginHistoryServiceImpl implements UserLoginHistoryService {

    private final UserLoginHistoryRepository userLoginHistoryRepository;

    public UserLoginHistoryServiceImpl(UserLoginHistoryRepository userLoginHistoryRepository) {
        this.userLoginHistoryRepository = userLoginHistoryRepository;
    }

    @Override
    public void save(UserLoginHistory userLoginHistory) {
        userLoginHistoryRepository.save(userLoginHistory);
    }
}
