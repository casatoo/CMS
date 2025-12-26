package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestDTO;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestResponseDTO;
import io.github.mskim.comm.cms.sp.UserAttendanceChangeRequestSP;

import java.util.List;

public interface UserAttendanceChangeRequestService {

    ApiResponse attendanceChangeRequest(UserAttendanceChangeRequestDTO request);

    List<UserAttendanceChangeRequestResponseDTO> findAllChangeRequests();

    List<UserAttendanceChangeRequestResponseDTO> searchAttendanceChangeRequests(UserAttendanceChangeRequestSP sp);

    ApiResponse approveRequest(String requestId);

    ApiResponse rejectRequest(String requestId, String rejectReason);
}
