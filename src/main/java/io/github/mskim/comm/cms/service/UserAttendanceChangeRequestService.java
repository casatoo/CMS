package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestDTO;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestResponseDTO;

import java.util.List;

public interface UserAttendanceChangeRequestService {

    ApiResponse attendanceChangeRequest(UserAttendanceChangeRequestDTO request);

    List<UserAttendanceChangeRequestResponseDTO> findAllChangeRequests();
}
