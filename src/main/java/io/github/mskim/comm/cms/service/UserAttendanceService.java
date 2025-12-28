package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.dto.AttendanceHistoryDTO;
import io.github.mskim.comm.cms.entity.UserAttendance;
import io.github.mskim.comm.cms.sp.UserAttendanceSP;
import io.github.mskim.comm.cms.dto.UserAttendanceDTO;

import java.time.LocalDate;
import java.util.List;

public interface UserAttendanceService {

    void checkIn();

    void checkOut();

    UserAttendanceDTO findTodayCheckInTime(String userId);

    int countWorkDaysThisMonth(String userId);

    List<UserAttendanceDTO> findAllUserAttendanceThisMonth(UserAttendanceSP userAttendanceSP);

    UserAttendance findAttendanceByDate (UserAttendanceSP userAttendanceSP);

    /**
     * 출퇴근 기록 조회 (휴가 상태 포함)
     *
     * @param workDate 근무일자
     * @param userName 사용자명 (선택)
     * @return 출퇴근 기록 목록 (휴가 상태 포함)
     */
    List<AttendanceHistoryDTO> findAttendanceHistory(LocalDate workDate, String userName);
}
