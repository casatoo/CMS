package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.dto.PageResponse;
import io.github.mskim.comm.cms.dto.UserLeaveRequestDTO;
import io.github.mskim.comm.cms.dto.UserLeaveRequestResponseDTO;
import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.repository.UserLeaveRequestRepository;
import io.github.mskim.comm.cms.repository.UserRepository;
import io.github.mskim.comm.cms.repository.specification.UserLeaveRequestSpecification;
import io.github.mskim.comm.cms.service.UserLeaveRequestService;
import io.github.mskim.comm.cms.sp.UserLeaveRequestSP;
import io.github.mskim.comm.cms.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserLeaveRequestServiceImpl implements UserLeaveRequestService {

    private final UserLeaveRequestRepository userLeaveRequestRepository;
    private final SecurityContextUtil securityContextUtil;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ApiResponse createLeaveRequest(UserLeaveRequestDTO request) {
        Users user = securityContextUtil.getCurrentUser();

        // 동일 날짜에 신청중인 건이 있는지 확인
        Optional<UserLeaveRequest> existingRequest = userLeaveRequestRepository
                .findPendingRequestByUserIdAndDate(user.getId(), request.getRequestDate());

        if (existingRequest.isPresent()) {
            return ApiResponse.of(ApiStatus.OK, "이미 해당 날짜에 신청중인 내역이 있습니다.", false);
        }

        // 연차 신청 시 남은 연차 확인
        if (request.getLeaveType() == UserLeaveRequest.LeaveType.ANNUAL_LEAVE) {
            if (user.getAnnualLeaveDays() <= 0) {
                return ApiResponse.of(ApiStatus.OK, "남은 연차가 없습니다.", false);
            }
        }

        // 출장, 외근의 경우 장소 필수
        if ((request.getLeaveType() == UserLeaveRequest.LeaveType.BUSINESS_TRIP ||
                request.getLeaveType() == UserLeaveRequest.LeaveType.FIELD_WORK) &&
                (request.getLocation() == null || request.getLocation().trim().isEmpty())) {
            return ApiResponse.of(ApiStatus.OK, "출장/외근 신청 시 장소는 필수입니다.", false);
        }

        UserLeaveRequest leaveRequest = UserLeaveRequest.builder()
                .user(user)
                .leaveType(request.getLeaveType())
                .requestDate(request.getRequestDate())
                .periodType(request.getPeriodType())
                .location(request.getLocation())
                .reason(request.getReason())
                .status(UserLeaveRequest.RequestStatus.REQUEST)
                .build();

        userLeaveRequestRepository.save(leaveRequest);

        return ApiResponse.of(ApiStatus.OK, "신청완료", true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLeaveRequestResponseDTO> findAllLeaveRequests() {
        List<UserLeaveRequest> requests = userLeaveRequestRepository.findAll();

        return requests.stream()
                .map(request -> {
                    // Lazy loading 강제 초기화
                    if (request.getUser() != null) {
                        request.getUser().getName();
                    }
                    if (request.getApprover() != null) {
                        request.getApprover().getName();
                    }
                    return UserLeaveRequestResponseDTO.from(request);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLeaveRequestResponseDTO> searchLeaveRequests(UserLeaveRequestSP sp) {
        // 현재 로그인한 사용자 확인
        Users currentUser = securityContextUtil.getCurrentUser();

        // 일반 사용자는 자신의 데이터만 조회 가능 (관리자는 모든 데이터 조회 가능)
        if (!"ROLE_ADMIN".equals(currentUser.getRole())) {
            sp.setUserId(currentUser.getId());
        }

        List<UserLeaveRequest> requests = userLeaveRequestRepository.searchLeaveRequests(sp);

        return requests.stream()
                .map(request -> {
                    // Lazy loading 강제 초기화
                    if (request.getUser() != null) {
                        request.getUser().getName();
                    }
                    if (request.getApprover() != null) {
                        request.getApprover().getName();
                    }
                    return UserLeaveRequestResponseDTO.from(request);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserLeaveRequestResponseDTO> searchLeaveRequestsWithPaging(
            UserLeaveRequestSP sp,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        // 현재 로그인한 사용자 확인
        Users currentUser = securityContextUtil.getCurrentUser();

        // 일반 사용자는 자신의 데이터만 조회 가능 (관리자는 모든 데이터 조회 가능)
        if (!"ROLE_ADMIN".equals(currentUser.getRole())) {
            sp.setUserId(currentUser.getId());
        }

        // 정렬 설정
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy != null ? sortBy : "createdAt");

        // 페이지 요청 생성
        Pageable pageable = PageRequest.of(page, size, sort);

        // Specification 생성
        Specification<UserLeaveRequest> spec = UserLeaveRequestSpecification.createSpecification(sp);

        // 페이징 조회
        Page<UserLeaveRequest> entityPage = userLeaveRequestRepository.findAll(spec, pageable);

        // DTO 변환
        Page<UserLeaveRequestResponseDTO> dtoPage = entityPage.map(UserLeaveRequestResponseDTO::from);

        // PageResponse 변환
        return PageResponse.from(dtoPage);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "dailyAttendance", allEntries = true),
        @CacheEvict(value = "attendanceSummary", allEntries = true),
        @CacheEvict(value = "attendanceList", allEntries = true)
    })
    public ApiResponse approveRequest(String requestId) {
        // 현재 로그인한 사용자 정보
        Users approver = securityContextUtil.getCurrentUser();

        // 신청 내역 조회 (User와 함께 조회하여 Lazy Loading 방지)
        Optional<UserLeaveRequest> optionalRequest = userLeaveRequestRepository.findByIdWithUser(requestId);
        if (optionalRequest.isEmpty()) {
            return ApiResponse.of(ApiStatus.NOT_FOUND, "신청 내역을 찾을 수 없습니다.", false);
        }

        UserLeaveRequest request = optionalRequest.get();

        // 이미 처리된 신청인지 확인
        if (request.getStatus() != UserLeaveRequest.RequestStatus.REQUEST) {
            return ApiResponse.of(ApiStatus.BAD_REQUEST, "이미 처리된 신청입니다.", false);
        }

        // 승인 처리
        request.setStatus(UserLeaveRequest.RequestStatus.APPROVE);
        request.setApprover(approver);
        request.setApprovedAt(LocalDateTime.now());
        userLeaveRequestRepository.save(request);

        // 연차인 경우 사용자의 연차 차감
        if (request.getLeaveType() == UserLeaveRequest.LeaveType.ANNUAL_LEAVE) {
            Users user = request.getUser();
            double deductDays = 0;

            // 기간에 따라 차감일 계산
            switch (request.getPeriodType()) {
                case ALL_DAY:
                    deductDays = 1.0;
                    break;
                case MORNING:
                case AFTERNOON:
                    deductDays = 0.5;
                    break;
            }

            // 연차 차감
            Double currentLeaveDays = user.getAnnualLeaveDays();
            Double newLeaveDays = currentLeaveDays - deductDays;
            user.setAnnualLeaveDays(newLeaveDays);
            userRepository.save(user);
        }

        return ApiResponse.of(ApiStatus.OK, "승인되었습니다.", true);
    }

    @Override
    @Transactional
    public ApiResponse rejectRequest(String requestId, String rejectReason) {
        // 현재 로그인한 사용자 정보
        Users approver = securityContextUtil.getCurrentUser();

        // 신청 내역 조회 (User와 함께 조회하여 Lazy Loading 방지)
        Optional<UserLeaveRequest> optionalRequest = userLeaveRequestRepository.findByIdWithUser(requestId);
        if (optionalRequest.isEmpty()) {
            return ApiResponse.of(ApiStatus.NOT_FOUND, "신청 내역을 찾을 수 없습니다.", false);
        }

        UserLeaveRequest request = optionalRequest.get();

        // 이미 처리된 신청인지 확인
        if (request.getStatus() != UserLeaveRequest.RequestStatus.REQUEST) {
            return ApiResponse.of(ApiStatus.BAD_REQUEST, "이미 처리된 신청입니다.", false);
        }

        // 반려 사유 확인
        if (rejectReason == null || rejectReason.trim().isEmpty()) {
            return ApiResponse.of(ApiStatus.BAD_REQUEST, "반려 사유를 입력해주세요.", false);
        }

        // 반려 처리
        request.setStatus(UserLeaveRequest.RequestStatus.REJECT);
        request.setApprover(approver);
        // 반려 시에는 승인일시를 설정하지 않음
        request.setReason(request.getReason() + "\n[반려 사유] " + rejectReason);
        userLeaveRequestRepository.save(request);

        return ApiResponse.of(ApiStatus.OK, "반려되었습니다.", true);
    }

    @Override
    public List<UserLeaveRequestResponseDTO> findMyLeaveRequests() {
        Users user = securityContextUtil.getCurrentUser();
        UserLeaveRequestSP sp = new UserLeaveRequestSP();
        sp.setUserId(user.getId());
        List<UserLeaveRequest> requests = userLeaveRequestRepository.searchLeaveRequests(sp);

        return requests.stream()
                .map(request -> {
                    // Lazy Loading 초기화
                    request.getUser().getName();
                    if (request.getApprover() != null) {
                        request.getApprover().getName();
                    }
                    return UserLeaveRequestResponseDTO.from(request);
                })
                .collect(Collectors.toList());
    }
}
