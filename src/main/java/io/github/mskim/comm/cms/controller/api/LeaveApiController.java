package io.github.mskim.comm.cms.controller.api;

import io.github.mskim.comm.cms.api.ApiPaths;
import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.LeaveApprovalRequestDTO;
import io.github.mskim.comm.cms.dto.PageResponse;
import io.github.mskim.comm.cms.dto.UserLeaveRequestDTO;
import io.github.mskim.comm.cms.dto.UserLeaveRequestResponseDTO;
import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import io.github.mskim.comm.cms.service.UserLeaveRequestService;
import io.github.mskim.comm.cms.sp.UserLeaveRequestSP;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.LEAVE)
@Validated
public class LeaveApiController {

    private final UserLeaveRequestService userLeaveRequestService;

    @PostMapping("/request")
    public ApiResponse createLeaveRequest(@Valid @RequestBody UserLeaveRequestDTO request) {
        return userLeaveRequestService.createLeaveRequest(request);
    }

    @GetMapping("/request/all")
    public List<UserLeaveRequestResponseDTO> findAllLeaveRequests() {
        return userLeaveRequestService.findAllLeaveRequests();
    }

    @GetMapping("/request/search")
    public List<UserLeaveRequestResponseDTO> searchLeaveRequests(
            @RequestParam(value = "status", required = false) UserLeaveRequest.RequestStatus status,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "leaveType", required = false) UserLeaveRequest.LeaveType leaveType,
            @RequestParam(value = "periodType", required = false) UserLeaveRequest.PeriodType periodType,
            @RequestParam(value = "requestDateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDateStart,
            @RequestParam(value = "requestDateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDateEnd,
            @RequestParam(value = "createdAtStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd
    ) {
        UserLeaveRequestSP sp = new UserLeaveRequestSP();
        sp.setStatus(status);
        sp.setUserName(userName);
        sp.setLeaveType(leaveType);
        sp.setPeriodType(periodType);
        sp.setRequestDateStart(requestDateStart);
        sp.setRequestDateEnd(requestDateEnd);
        sp.setCreatedAtStart(createdAtStart);
        sp.setCreatedAtEnd(createdAtEnd);

        return userLeaveRequestService.searchLeaveRequests(sp);
    }

    /**
     * 페이징을 지원하는 휴가 요청 검색 엔드포인트
     *
     * @param status 신청 상태
     * @param userName 신청자명
     * @param leaveType 휴가 유형
     * @param periodType 기간 유형
     * @param requestDateStart 신청 날짜 시작
     * @param requestDateEnd 신청 날짜 종료
     * @param createdAtStart 생성일시 시작
     * @param createdAtEnd 생성일시 종료
     * @param page 페이지 번호 (0부터 시작, 기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 컬럼 (기본값: createdAt)
     * @param direction 정렬 방향 (asc/desc, 기본값: desc)
     * @return 페이징된 휴가 요청 목록
     */
    @GetMapping("/request/search/page")
    public PageResponse<UserLeaveRequestResponseDTO> searchLeaveRequestsWithPaging(
            @RequestParam(value = "status", required = false) UserLeaveRequest.RequestStatus status,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "leaveType", required = false) UserLeaveRequest.LeaveType leaveType,
            @RequestParam(value = "periodType", required = false) UserLeaveRequest.PeriodType periodType,
            @RequestParam(value = "requestDateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDateStart,
            @RequestParam(value = "requestDateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDateEnd,
            @RequestParam(value = "createdAtStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
            @RequestParam(value = "createdAtEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    ) {
        UserLeaveRequestSP sp = new UserLeaveRequestSP();
        sp.setStatus(status);
        sp.setUserName(userName);
        sp.setLeaveType(leaveType);
        sp.setPeriodType(periodType);
        sp.setRequestDateStart(requestDateStart);
        sp.setRequestDateEnd(requestDateEnd);
        sp.setCreatedAtStart(createdAtStart);
        sp.setCreatedAtEnd(createdAtEnd);

        return userLeaveRequestService.searchLeaveRequestsWithPaging(sp, page, size, sortBy, direction);
    }

    @PostMapping("/request/approve")
    public ApiResponse approveLeaveRequest(@Valid @RequestBody LeaveApprovalRequestDTO request) {
        return userLeaveRequestService.approveRequest(request.getRequestId());
    }

    @PostMapping("/request/reject")
    public ApiResponse rejectLeaveRequest(@Valid @RequestBody LeaveApprovalRequestDTO request) {
        return userLeaveRequestService.rejectRequest(request.getRequestId(), request.getRejectReason());
    }
}
