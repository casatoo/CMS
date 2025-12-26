package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestDTO;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestResponseDTO;
import io.github.mskim.comm.cms.entity.UserAttendance;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.repository.UserAttendanceRepository;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import io.github.mskim.comm.cms.sp.UserAttendanceChangeRequestSP;
import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import io.github.mskim.comm.cms.repository.UserAttendanceChangeRequestRepository;
import io.github.mskim.comm.cms.service.UserAttendanceChangeRequestService;
import io.github.mskim.comm.cms.sp.UserAttendanceSP;
import io.github.mskim.comm.cms.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 근태 수정 요청 서비스 구현
 *
 * <p>MyBatis Mapper 의존성을 제거하고 JPA Repository로 완전 마이그레이션</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAttendanceChangeRequestServiceImpl implements UserAttendanceChangeRequestService {

    private final UserAttendanceChangeRequestRepository userAttendanceChangeRequestRepository;
    private final UserAttendanceService userAttendanceService;
    private final SecurityContextUtil securityContextUtil;
    private final UserAttendanceRepository userAttendanceRepository;

    @Override
    @Transactional
    public ApiResponse attendanceChangeRequest(UserAttendanceChangeRequestDTO request) {

        // 동일 날짜에 신청중인 건이 있는지 조회
        Users user = securityContextUtil.getCurrentUser();

        // JPA Repository 사용 (MyBatis 제거)
        Optional<UserAttendanceChangeRequest> existingRequest = userAttendanceChangeRequestRepository
            .findOneBySearchParams(
                user.getId(),
                request.getAttendance().getWorkDate(),
                request.getStatus().name()
            );

        if (existingRequest.isPresent()) {
            return ApiResponse.of(ApiStatus.OK, "이미 변경신청중입니다.", false);
        }
        UserAttendanceSP userAttendanceSP = new UserAttendanceSP();
        userAttendanceSP.setUserId(user.getId());
        userAttendanceSP.setWorkDate(request.getAttendance().getWorkDate());
        UserAttendance userAttendance = userAttendanceService.findAttendanceByDate(userAttendanceSP);

        // 출근 시간과 퇴근 시간이 없을 수 있으므로 null-safe 하게 처리
        LocalDateTime originalCheckInTime = userAttendance != null ? userAttendance.getCheckInTime() : null;
        LocalDateTime originalCheckOutTime = userAttendance != null ? userAttendance.getCheckOutTime() : null;

        UserAttendanceChangeRequest data = UserAttendanceChangeRequest.builder()
                .attendance(userAttendance) // userAttendance 자체도 null일 수 있음
                .approvedAt(LocalDateTime.now())
                .user(user)
                .workDate(request.getAttendance().getWorkDate())
                .originalCheckInTime(originalCheckInTime)
                .originalCheckOutTime(originalCheckOutTime)
                .reason(request.getReason())
                .requestedCheckInTime(request.getRequestedCheckInTime())
                .requestedCheckOutTime(request.getRequestedCheckOutTime())
                .status(request.getStatus())
                .build();
        userAttendanceChangeRequestRepository.save(data);

        return ApiResponse.of(ApiStatus.OK, "신청완료", true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAttendanceChangeRequestResponseDTO> findAllChangeRequests() {
        List<UserAttendanceChangeRequest> requests = userAttendanceChangeRequestRepository.findAll();

        return requests.stream()
                .map(request -> {
                    // Lazy loading 강제 초기화
                    if (request.getUser() != null) {
                        request.getUser().getName();
                    }
                    if (request.getApprover() != null) {
                        request.getApprover().getName();
                    }
                    return UserAttendanceChangeRequestResponseDTO.from(request);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAttendanceChangeRequestResponseDTO> searchAttendanceChangeRequests(UserAttendanceChangeRequestSP sp) {
        List<UserAttendanceChangeRequest> requests = userAttendanceChangeRequestRepository.searchAttendanceChangeRequests(sp);

        return requests.stream()
                .map(request -> {
                    // Lazy loading 강제 초기화
                    if (request.getUser() != null) {
                        request.getUser().getName();
                    }
                    if (request.getApprover() != null) {
                        request.getApprover().getName();
                    }
                    if (request.getAttendance() != null) {
                        request.getAttendance().getWorkDate();
                    }
                    return UserAttendanceChangeRequestResponseDTO.from(request);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ApiResponse approveRequest(String requestId) {
        // 현재 로그인한 사용자 정보
        Users approver = securityContextUtil.getCurrentUser();

        // 신청 내역 조회
        Optional<UserAttendanceChangeRequest> optionalRequest = userAttendanceChangeRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            return ApiResponse.of(ApiStatus.NOT_FOUND, "신청 내역을 찾을 수 없습니다.", false);
        }

        UserAttendanceChangeRequest request = optionalRequest.get();

        // 이미 처리된 신청인지 확인
        if (request.getStatus() != UserAttendanceChangeRequest.ChangeStatus.REQUEST) {
            return ApiResponse.of(ApiStatus.BAD_REQUEST, "이미 처리된 신청입니다.", false);
        }

        // 승인 처리
        request.setStatus(UserAttendanceChangeRequest.ChangeStatus.APPROVE);
        request.setApprover(approver);
        request.setApprovedAt(LocalDateTime.now());
        userAttendanceChangeRequestRepository.save(request);

        // 근태 기록 업데이트
        if (request.getAttendance() != null) {
            UserAttendance attendance = request.getAttendance();
            attendance.setCheckInTime(request.getRequestedCheckInTime());
            if (request.getRequestedCheckOutTime() != null) {
                attendance.setCheckOutTime(request.getRequestedCheckOutTime());
            }
            userAttendanceRepository.save(attendance);
        }

        return ApiResponse.of(ApiStatus.OK, "승인되었습니다.", true);
    }

    @Override
    @Transactional
    public ApiResponse rejectRequest(String requestId, String rejectReason) {
        // 현재 로그인한 사용자 정보
        Users approver = securityContextUtil.getCurrentUser();

        // 신청 내역 조회
        Optional<UserAttendanceChangeRequest> optionalRequest = userAttendanceChangeRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            return ApiResponse.of(ApiStatus.NOT_FOUND, "신청 내역을 찾을 수 없습니다.", false);
        }

        UserAttendanceChangeRequest request = optionalRequest.get();

        // 이미 처리된 신청인지 확인
        if (request.getStatus() != UserAttendanceChangeRequest.ChangeStatus.REQUEST) {
            return ApiResponse.of(ApiStatus.BAD_REQUEST, "이미 처리된 신청입니다.", false);
        }

        // 반려 사유 확인
        if (rejectReason == null || rejectReason.trim().isEmpty()) {
            return ApiResponse.of(ApiStatus.BAD_REQUEST, "반려 사유를 입력해주세요.", false);
        }

        // 반려 처리
        request.setStatus(UserAttendanceChangeRequest.ChangeStatus.REJECT);
        request.setApprover(approver);
        request.setApprovedAt(LocalDateTime.now());
        request.setReason(request.getReason() + "\n[반려 사유] " + rejectReason);
        userAttendanceChangeRequestRepository.save(request);

        return ApiResponse.of(ApiStatus.OK, "반려되었습니다.", true);
    }
}
