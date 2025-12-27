package io.github.mskim.comm.cms.controller.api;

import io.github.mskim.comm.cms.api.ApiPaths;
import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.dto.ApprovalRequestDTO;
import io.github.mskim.comm.cms.dto.PageResponse;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestDTO;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestResponseDTO;
import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import io.github.mskim.comm.cms.sp.UserAttendanceChangeRequestSP;
import io.github.mskim.comm.cms.sp.UserAttendanceSP;
import io.github.mskim.comm.cms.dto.UserAttendanceDTO;
import io.github.mskim.comm.cms.service.UserAttendanceChangeRequestService;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.ATTENDANCE)
@Validated
public class AttendanceApiController {

    private final UserAttendanceService userAttendanceService;
    private final UserAttendanceChangeRequestService userAttendanceChangeRequestService;

    @GetMapping("/month/all")
    public List<UserAttendanceDTO> findAllUserAttendanceThisMonth(
            @RequestParam("startDate")
            @NotBlank(message = "시작 날짜는 필수입니다")
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다")
            String startDate,
            @RequestParam("endDate")
            @NotBlank(message = "종료 날짜는 필수입니다")
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다")
            String endDate) {
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
    public ApiResponse attendanceChangeRequest(@Valid @RequestBody UserAttendanceChangeRequestDTO request) {
        return userAttendanceChangeRequestService.attendanceChangeRequest(request);
    }

    @GetMapping("/request/all")
    public List<UserAttendanceChangeRequestResponseDTO> findAllChangeRequests() {
        return userAttendanceChangeRequestService.findAllChangeRequests();
    }

    @GetMapping("/request/search")
    public List<UserAttendanceChangeRequestResponseDTO> searchAttendanceChangeRequests(
            @RequestParam(value = "status", required = false) UserAttendanceChangeRequest.ChangeStatus status,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "workDateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDateStart,
            @RequestParam(value = "workDateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDateEnd,
            @RequestParam(value = "createdAtStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd
    ) {
        UserAttendanceChangeRequestSP sp = new UserAttendanceChangeRequestSP();
        sp.setStatus(status);
        sp.setUserName(userName);
        sp.setWorkDateStart(workDateStart);
        sp.setWorkDateEnd(workDateEnd);
        sp.setCreatedAtStart(createdAtStart);
        sp.setCreatedAtEnd(createdAtEnd);

        return userAttendanceChangeRequestService.searchAttendanceChangeRequests(sp);
    }

    /**
     * 페이징을 지원하는 근태 변경 요청 검색 엔드포인트
     *
     * @param status 신청 상태
     * @param userName 신청자명
     * @param workDateStart 근무일 시작
     * @param workDateEnd 근무일 종료
     * @param createdAtStart 생성일시 시작
     * @param createdAtEnd 생성일시 종료
     * @param page 페이지 번호 (0부터 시작, 기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 컬럼 (기본값: createdAt)
     * @param direction 정렬 방향 (asc/desc, 기본값: desc)
     * @return 페이징된 근태 변경 요청 목록
     */
    @GetMapping("/request/search/page")
    public PageResponse<UserAttendanceChangeRequestResponseDTO> searchAttendanceChangeRequestsWithPaging(
            @RequestParam(value = "status", required = false) UserAttendanceChangeRequest.ChangeStatus status,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "workDateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDateStart,
            @RequestParam(value = "workDateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDateEnd,
            @RequestParam(value = "createdAtStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    ) {
        UserAttendanceChangeRequestSP sp = new UserAttendanceChangeRequestSP();
        sp.setStatus(status);
        sp.setUserName(userName);
        sp.setWorkDateStart(workDateStart);
        sp.setWorkDateEnd(workDateEnd);
        sp.setCreatedAtStart(createdAtStart);
        sp.setCreatedAtEnd(createdAtEnd);

        return userAttendanceChangeRequestService.searchAttendanceChangeRequestsWithPaging(sp, page, size, sortBy, direction);
    }

    @PostMapping("/request/approve")
    public ApiResponse approveAttendanceChangeRequest(@Valid @RequestBody ApprovalRequestDTO request) {
        return userAttendanceChangeRequestService.approveRequest(request.getRequestId());
    }

    @PostMapping("/request/reject")
    public ApiResponse rejectAttendanceChangeRequest(@Valid @RequestBody ApprovalRequestDTO request) {
        return userAttendanceChangeRequestService.rejectRequest(request.getRequestId(), request.getRejectReason());
    }
}
