package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestDTO;
import io.github.mskim.comm.cms.dto.UserAttendanceChangeRequestResponseDTO;
import io.github.mskim.comm.cms.entity.UserAttendance;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import io.github.mskim.comm.cms.service.UserService;
import io.github.mskim.comm.cms.sp.UserAttendanceChangeRequestSP;
import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import io.github.mskim.comm.cms.mapper.UserAttendanceChangeRequestMapper;
import io.github.mskim.comm.cms.repository.UserAttendanceChangeRequestRepository;
import io.github.mskim.comm.cms.service.UserAttendanceChangeRequestService;
import io.github.mskim.comm.cms.sp.UserAttendanceSP;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAttendanceChangeRequestServiceImpl implements UserAttendanceChangeRequestService {

    private final UserAttendanceChangeRequestRepository userAttendanceChangeRequestRepository;
    private final UserAttendanceChangeRequestMapper userAttendanceChangeRequestMapper;
    private final UserAttendanceService userAttendanceService;
    private final UserService userService;

    public UserAttendanceChangeRequestServiceImpl(UserAttendanceChangeRequestRepository userAttendanceChangeRequestRepository,
                                                  UserAttendanceChangeRequestMapper userAttendanceChangeRequestMapper,
                                                  UserAttendanceService userAttendanceService,
                                                  UserService userService) {

        this.userAttendanceChangeRequestRepository = userAttendanceChangeRequestRepository;
        this.userAttendanceChangeRequestMapper = userAttendanceChangeRequestMapper;
        this.userAttendanceService = userAttendanceService;
        this.userService = userService;
    }

    @Override
    public ApiResponse attendanceChangeRequest(UserAttendanceChangeRequestDTO request) {

        // 동일 날짜에 신청중인 건이 있는지 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        Users user = userService.findByLoginId(loginId);

        UserAttendanceChangeRequestSP searchParams = new UserAttendanceChangeRequestSP();
        searchParams.setUserId(user.getId());
        searchParams.setWorkDate(request.getAttendance().getWorkDate());
        searchParams.setStatus(request.getStatus());
        UserAttendanceChangeRequest userAttendanceChangeRequest = userAttendanceChangeRequestMapper.findOneBySearchParams(searchParams);

        if (!ObjectUtils.isEmpty(userAttendanceChangeRequest)) {
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
}
