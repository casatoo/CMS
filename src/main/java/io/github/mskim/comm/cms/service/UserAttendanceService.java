package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.dto.UserAttendanceDTO;

public interface UserAttendanceService {

    void checkIn();

    void checkOut();

    UserAttendanceDTO findTodayCheckInTime(String userId);

    int countWorkDaysThisMonth(String userId);
}
