package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.dto.PageResponse;
import io.github.mskim.comm.cms.dto.UserLeaveRequestDTO;
import io.github.mskim.comm.cms.dto.UserLeaveRequestResponseDTO;
import io.github.mskim.comm.cms.sp.UserLeaveRequestSP;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserLeaveRequestService {

    ApiResponse createLeaveRequest(UserLeaveRequestDTO request);

    List<UserLeaveRequestResponseDTO> findAllLeaveRequests();

    List<UserLeaveRequestResponseDTO> searchLeaveRequests(UserLeaveRequestSP sp);

    /**
     * 페이징을 지원하는 휴가 요청 검색
     *
     * @param sp 검색 파라미터
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param sortBy 정렬 기준 컬럼명
     * @param direction 정렬 방향 (asc/desc)
     * @return 페이징된 휴가 요청 목록
     */
    PageResponse<UserLeaveRequestResponseDTO> searchLeaveRequestsWithPaging(
            UserLeaveRequestSP sp,
            int page,
            int size,
            String sortBy,
            String direction
    );

    ApiResponse approveRequest(String requestId);

    ApiResponse rejectRequest(String requestId, String rejectReason);

    /**
     * 현재 로그인한 사용자의 연차/외근/출장 요청 조회
     *
     * @return 사용자의 휴가 요청 목록
     */
    List<UserLeaveRequestResponseDTO> findMyLeaveRequests();
}
