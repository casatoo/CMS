package io.github.mskim.comm.cms.mapper;

import io.github.mskim.comm.cms.sp.UserAttendanceSP;
import io.github.mskim.comm.cms.entity.UserAttendance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAttendanceMapper {

    UserAttendance findTodayCheckInTime(String userId);

    int countWorkDaysThisMonth(String userId);

    UserAttendance findAttendanceByDate(UserAttendanceSP userAttendanceSP);

    List<UserAttendance> findAttendanceInRange(UserAttendanceSP userAttendanceSP);
}
