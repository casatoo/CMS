package io.github.mskim.comm.cms.mapper;

import io.github.mskim.comm.cms.entity.UserLoginHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserLoginHistoryMapper {

    List<UserLoginHistory> findByUserId(@Param("loginId") String loginId);

}
