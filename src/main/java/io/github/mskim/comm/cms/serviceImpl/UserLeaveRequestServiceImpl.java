package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.dto.UserLeaveRequestDTO;
import io.github.mskim.comm.cms.dto.UserLeaveRequestResponseDTO;
import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.repository.UserLeaveRequestRepository;
import io.github.mskim.comm.cms.service.UserLeaveRequestService;
import io.github.mskim.comm.cms.service.UserService;
import io.github.mskim.comm.cms.sp.UserLeaveRequestSP;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserLeaveRequestServiceImpl implements UserLeaveRequestService {

    private final UserLeaveRequestRepository userLeaveRequestRepository;
    private final UserService userService;

    public UserLeaveRequestServiceImpl(UserLeaveRequestRepository userLeaveRequestRepository,
                                       UserService userService) {
        this.userLeaveRequestRepository = userLeaveRequestRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ApiResponse createLeaveRequest(UserLeaveRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        Users user = userService.findByLoginId(loginId);

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
}
