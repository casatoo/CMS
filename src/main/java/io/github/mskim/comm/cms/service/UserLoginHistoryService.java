package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.entity.UserLoginHistory;

import java.util.List;

public interface UserLoginHistoryService {

    void save(UserLoginHistory userLoginHistory);

    List<UserLoginHistory> findByUserId(String userId);
}
