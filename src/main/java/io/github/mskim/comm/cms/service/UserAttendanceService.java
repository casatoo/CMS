package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.dto.SearchParams;
import io.github.mskim.comm.cms.dto.UserAttendanceDTO;

import java.util.List;

public interface UserAttendanceService {

    void checkIn();

    void checkOut();

    UserAttendanceDTO findTodayCheckInTime(String userId);

    int countWorkDaysThisMonth(String userId);

    List<UserAttendanceDTO> findAllUserAttendanceThisMonth(SearchParams searchParams);
}
