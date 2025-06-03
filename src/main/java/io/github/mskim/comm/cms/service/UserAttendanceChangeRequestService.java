package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestDTO;

public interface UserAttendanceChangeRequestService {

    ApiResponse attendanceChangeRequest(UserAttendanceChangeRequestDTO request);
}
