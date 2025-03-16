package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, String> {

}
