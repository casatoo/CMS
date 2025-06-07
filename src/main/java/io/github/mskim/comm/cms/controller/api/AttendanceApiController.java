package io.github.mskim.comm.cms.controller.api;

import io.github.mskim.comm.cms.api.ApiPaths;
import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestDTO;
import io.github.mskim.comm.cms.sp.UserAttendanceSP;
import io.github.mskim.comm.cms.dto.UserAttendanceDTO;
import io.github.mskim.comm.cms.service.UserAttendanceChangeRequestService;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.ATTENDANCE)
public class AttendanceApiController {

    private final UserAttendanceService userAttendanceService;
    private final UserAttendanceChangeRequestService userAttendanceChangeRequestService;

    @GetMapping("/month/all")
    public List<UserAttendanceDTO> findAllUserAttendanceThisMonth(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        // 날짜 형식 정의 (예: "yyyy-MM-dd" 형태로 날짜가 오는 경우)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, formatter);
        UserAttendanceSP userAttendanceSP = new UserAttendanceSP();
        userAttendanceSP.setStartDate(startLocalDate);
        userAttendanceSP.setEndDate(endLocalDate);

        return userAttendanceService.findAllUserAttendanceThisMonth(userAttendanceSP);
    }

    @PostMapping("/check-in")
    public ApiResponse checkIn() {
        try {
            userAttendanceService.checkIn();
            return ApiResponse.of(ApiStatus.OK, "출근 체크 하였습니다.");
        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.INTERNAL_SERVER_ERROR, "출근 체크 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/check-out")
    public ApiResponse checkOut() {
        try {
            userAttendanceService.checkOut();
            return ApiResponse.of(ApiStatus.OK, "퇴근 체크 하였습니다.");
        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.INTERNAL_SERVER_ERROR, "퇴근 체크 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/change/request")
    public ApiResponse attendanceChangeRequest(@RequestBody UserAttendanceChangeRequestDTO request) {
        return userAttendanceChangeRequestService.attendanceChangeRequest(request);
    }
}
