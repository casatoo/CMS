package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestDTO;
import io.github.mskim.comm.cms.repository.UserAttendanceChangeRequestRepository;
import io.github.mskim.comm.cms.service.UserAttendanceChangeRequestService;
import org.springframework.stereotype.Service;

@Service
public class UserAttendanceChangeRequestServiceImpl implements UserAttendanceChangeRequestService {

    private final UserAttendanceChangeRequestRepository userAttendanceChangeRequestRepository;

    public UserAttendanceChangeRequestServiceImpl(UserAttendanceChangeRequestRepository userAttendanceChangeRequestRepository) {
        this.userAttendanceChangeRequestRepository = userAttendanceChangeRequestRepository;
    }

    @Override
    public ApiResponse attendanceChangeRequest(UserAttendanceChangeRequestDTO request) {
        // 동일 날짜에 신청중인 건이 있는지 조회

        // 이미 신청중인 경우

        // 신청완료
        return ApiResponse.of(ApiStatus.OK, "이미 변경신청중입니다.", false);
    }
}
