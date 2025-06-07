package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.entity.UserAttendance;
import io.github.mskim.comm.cms.sp.UserAttendanceSP;
import io.github.mskim.comm.cms.dto.UserAttendanceDTO;

import java.util.List;

public interface UserAttendanceService {

    void checkIn();

    void checkOut();

    UserAttendanceDTO findTodayCheckInTime(String userId);

    int countWorkDaysThisMonth(String userId);

    List<UserAttendanceDTO> findAllUserAttendanceThisMonth(UserAttendanceSP userAttendanceSP);

    UserAttendance findAttendanceByDate (UserAttendanceSP userAttendanceSP);
}
