package io.github.mskim.comm.cms.mapper;

import io.github.mskim.comm.cms.dto.SearchParams;
import io.github.mskim.comm.cms.dto.UserAttendanceDTO;
import io.github.mskim.comm.cms.entity.UserAttendance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAttendanceMapper {

    UserAttendance findTodayCheckInTime(String userId);

    int countWorkDaysThisMonth(String userId);

    List<UserAttendance> findAttendanceByDate(SearchParams searchParams);

    List<UserAttendance> findAttendanceInRange(SearchParams searchParams);

}
