package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.UserLeaveRequestDTO;
import io.github.mskim.comm.cms.dto.UserLeaveRequestResponseDTO;
import io.github.mskim.comm.cms.sp.UserLeaveRequestSP;

import java.util.List;

public interface UserLeaveRequestService {

    ApiResponse createLeaveRequest(UserLeaveRequestDTO request);

    List<UserLeaveRequestResponseDTO> findAllLeaveRequests();

    List<UserLeaveRequestResponseDTO> searchLeaveRequests(UserLeaveRequestSP sp);

    ApiResponse approveRequest(String requestId);

    ApiResponse rejectRequest(String requestId, String rejectReason);
}
