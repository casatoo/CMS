package io.github.mskim.comm.cms.mapper;

import io.github.mskim.comm.cms.sp.UserAttendanceChangeRequestSP;
import io.github.mskim.comm.cms.sp.UserAttendanceSP;
import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserAttendanceChangeRequestMapper {

    UserAttendanceChangeRequest findById(String id);

    List<UserAttendanceChangeRequest> findAllByUserId(String userId);

    List<UserAttendanceChangeRequest> findPendingRequests();

    UserAttendanceChangeRequest findOneBySearchParams(UserAttendanceChangeRequestSP params);

}
