package io.github.mskim.comm.cms.controller.api;

import io.github.mskim.comm.cms.api.ApiPaths;
import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.LeaveApprovalRequestDTO;
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

    @PostMapping("/request/approve")
    public ApiResponse approveLeaveRequest(@Valid @RequestBody LeaveApprovalRequestDTO request) {
        return userLeaveRequestService.approveRequest(request.getRequestId());
    }

    @PostMapping("/request/reject")
    public ApiResponse rejectLeaveRequest(@Valid @RequestBody LeaveApprovalRequestDTO request) {
        return userLeaveRequestService.rejectRequest(request.getRequestId(), request.getRejectReason());
    }
}
