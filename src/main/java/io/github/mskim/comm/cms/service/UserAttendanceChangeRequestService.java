package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.PageResponse;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestDTO;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestResponseDTO;
import io.github.mskim.comm.cms.sp.UserAttendanceChangeRequestSP;

import java.util.List;

public interface UserAttendanceChangeRequestService {

    ApiResponse attendanceChangeRequest(UserAttendanceChangeRequestDTO request);

    List<UserAttendanceChangeRequestResponseDTO> findAllChangeRequests();

    List<UserAttendanceChangeRequestResponseDTO> searchAttendanceChangeRequests(UserAttendanceChangeRequestSP sp);

    /**
     * 페이징을 지원하는 근태 변경 요청 검색
     *
     * @param sp 검색 파라미터
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param sortBy 정렬 기준 컬럼명
     * @param direction 정렬 방향 (asc/desc)
     * @return 페이징된 근태 변경 요청 목록
     */
    PageResponse<UserAttendanceChangeRequestResponseDTO> searchAttendanceChangeRequestsWithPaging(
            UserAttendanceChangeRequestSP sp,
            int page,
            int size,
            String sortBy,
            String direction
    );

    ApiResponse approveRequest(String requestId);

    ApiResponse rejectRequest(String requestId, String rejectReason);
}
