package io.github.mskim.comm.cms.mapper;

import io.github.mskim.comm.cms.entity.UserAttendance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAttendanceMapper {

    UserAttendance findTodayCheckInTime(String userId);

    int countWorkDaysThisMonth(String userId);

}
