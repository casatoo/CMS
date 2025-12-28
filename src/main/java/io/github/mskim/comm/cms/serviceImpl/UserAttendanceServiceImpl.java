package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.config.CustomModelMapper;
import io.github.mskim.comm.cms.dto.AttendanceHistoryDTO;
import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import io.github.mskim.comm.cms.repository.UserLeaveRequestRepository;
import io.github.mskim.comm.cms.repository.UserRepository;
import io.github.mskim.comm.cms.sp.UserAttendanceSP;
import io.github.mskim.comm.cms.dto.UserAttendanceDTO;
import io.github.mskim.comm.cms.entity.UserAttendance;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.repository.UserAttendanceRepository;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import io.github.mskim.comm.cms.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 사용자 근태 서비스 구현
 *
 * <p>MyBatis Mapper 의존성을 제거하고 JPA Repository로 완전 마이그레이션</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAttendanceServiceImpl implements UserAttendanceService {

    private final UserAttendanceRepository userAttendanceRepository;
    private final UserRepository userRepository;
    private final UserLeaveRequestRepository userLeaveRequestRepository;
    private final SecurityContextUtil securityContextUtil;

    /**
     * 오늘 출근 시간 조회 (일별 근태 캐싱)
     *
     * <p>조회 결과는 30분간 캐시됩니다.</p>
     * <p>캐시 키: "userId:today"</p>
     *
     * @param userId 사용자 ID
     * @return 오늘의 근태 정보
     */
    @Override
    @Cacheable(value = "dailyAttendance", key = "#userId + ':' + T(java.time.LocalDate).now()")
    public UserAttendanceDTO findTodayCheckInTime(String userId) {
        // JPA Repository 사용 (MyBatis 제거)
        Optional<UserAttendance> userAttendance = userAttendanceRepository
            .findTodayCheckInTime(userId, LocalDate.now());

        return userAttendance
            .map(ua -> CustomModelMapper.getInstance().map(ua, UserAttendanceDTO.class))
            .orElse(null);
    }

    /**
     * 이번 달 근무일수 조회 (월별 집계 캐싱)
     *
     * <p>조회 결과는 30분간 캐시됩니다.</p>
     * <p>캐시 키: "userId:yearMonth"</p>
     *
     * @param userId 사용자 ID
     * @return 이번 달 근무일수
     */
    @Override
    @Cacheable(value = "attendanceSummary", key = "#userId + ':' + T(java.time.YearMonth).now()")
    public int countWorkDaysThisMonth(String userId) {
        // JPA Repository 사용 (MyBatis 제거)
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        Long count = userAttendanceRepository.countWorkDaysThisMonth(
            userId,
            startOfMonth,
            endOfMonth
        );

        return count != null ? count.intValue() : 0;
    }

    /**
     * 출근 처리 (캐시 무효화)
     *
     * <p>출근 처리 시 관련 캐시를 모두 무효화합니다:</p>
     * <ul>
     *   <li>일별 근태 캐시 (dailyAttendance)</li>
     *   <li>월별 집계 캐시 (attendanceSummary)</li>
     *   <li>월별 목록 캐시 (attendanceList)</li>
     * </ul>
     *
     * @throws IllegalStateException 이미 출근 처리된 경우
     */
    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "dailyAttendance", allEntries = true),
        @CacheEvict(value = "attendanceSummary", allEntries = true),
        @CacheEvict(value = "attendanceList", allEntries = true)
    })
    public void checkIn() {
        Users user = securityContextUtil.getCurrentUser();

        // 중복 출근 방지
        if (userAttendanceRepository.existsByUserIdAndWorkDate(user.getId(), LocalDate.now())) {
            throw new IllegalStateException("이미 오늘 출근 처리되었습니다.");
        }

        UserAttendance userAttendance = UserAttendance.builder()
                .user(user)
                .workDate(LocalDate.now())
                .checkInTime(LocalDateTime.now())
                .build();

        userAttendanceRepository.save(userAttendance);
    }

    /**
     * 퇴근 처리 (캐시 무효화)
     *
     * <p>퇴근 처리 시 관련 캐시를 모두 무효화합니다:</p>
     * <ul>
     *   <li>일별 근태 캐시 (dailyAttendance)</li>
     *   <li>월별 집계 캐시 (attendanceSummary)</li>
     *   <li>월별 목록 캐시 (attendanceList)</li>
     * </ul>
     *
     * @throws IllegalArgumentException 출근 기록이 없는 경우
     * @throws IllegalStateException 이미 퇴근 처리된 경우
     */
    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "dailyAttendance", allEntries = true),
        @CacheEvict(value = "attendanceSummary", allEntries = true),
        @CacheEvict(value = "attendanceList", allEntries = true)
    })
    public void checkOut() {
        Users user = securityContextUtil.getCurrentUser();

        // JPA Repository 사용 (MyBatis 제거)
        UserAttendance userAttendance = userAttendanceRepository
            .findTodayCheckInTime(user.getId(), LocalDate.now())
            .orElseThrow(() -> new IllegalArgumentException("오늘 출근 기록이 없습니다."));

        // 이미 퇴근했는지 확인
        if (userAttendance.getCheckOutTime() != null) {
            throw new IllegalStateException("이미 퇴근 처리되었습니다.");
        }

        userAttendance.setCheckOutTime(LocalDateTime.now());

        userAttendanceRepository.save(userAttendance);
    }

    /**
     * 기간별 근태 목록 조회 (월별 목록 캐싱)
     *
     * <p>조회 결과는 30분간 캐시됩니다.</p>
     * <p>캐시 키: "startDate:endDate"</p>
     *
     * @param userAttendanceSP 검색 조건 (시작일, 종료일)
     * @return 근태 목록
     */
    @Override
    @Cacheable(value = "attendanceList",
               key = "#userAttendanceSP.startDate + ':' + #userAttendanceSP.endDate",
               condition = "#userAttendanceSP.startDate != null and #userAttendanceSP.endDate != null")
    public List<UserAttendanceDTO> findAllUserAttendanceThisMonth(UserAttendanceSP userAttendanceSP) {
        Users user = securityContextUtil.getCurrentUser();

        // JPA Repository 사용 (MyBatis 제거)
        List<UserAttendance> userAttendanceList = userAttendanceRepository.findAttendanceInRange(
            user.getId(),
            userAttendanceSP.getStartDate(),
            userAttendanceSP.getEndDate()
        );

        return userAttendanceList != null && !userAttendanceList.isEmpty()
            ? CustomModelMapper.mapList(userAttendanceList, UserAttendanceDTO.class)
            : List.of();
    }

    @Override
    public UserAttendance findAttendanceByDate(UserAttendanceSP userAttendanceSP) {
        // JPA Repository 사용 (MyBatis 제거)
        return userAttendanceRepository.findAttendanceByDate(
            userAttendanceSP.getUserId(),
            userAttendanceSP.getWorkDate()
        ).orElse(null);
    }

    /**
     * 출퇴근 기록 조회 (휴가 상태 포함)
     *
     * <p>날짜 또는 사용자명 중 하나는 필수입니다.</p>
     * <ul>
     *   <li>날짜만 입력: 해당 날짜의 전체 사용자 출퇴근 기록 조회</li>
     *   <li>날짜 + 사용자명: 해당 날짜의 특정 사용자 출퇴근 기록 조회</li>
     *   <li>사용자명만 입력: 해당 사용자의 전체 출퇴근 기록을 최신순으로 조회</li>
     * </ul>
     *
     * @param workDate 근무일자 (선택)
     * @param userName 사용자명 (선택)
     * @return 출퇴근 기록 목록 (휴가 상태 포함)
     */
    @Override
    public List<AttendanceHistoryDTO> findAttendanceHistory(LocalDate workDate, String userName) {
        // 유효성 검사: 날짜 또는 사용자명 중 하나는 필수
        if (workDate == null && (userName == null || userName.trim().isEmpty())) {
            throw new IllegalArgumentException("날짜 또는 사용자명 중 하나는 필수입니다.");
        }

        // 케이스 3: 사용자명만 입력된 경우 - 해당 사용자의 전체 기록 조회 (최신순)
        if (workDate == null && userName != null && !userName.trim().isEmpty()) {
            return findAttendanceHistoryByUserName(userName.trim());
        }

        // 케이스 1, 2: 날짜가 입력된 경우 - 해당 날짜의 사용자 기록 조회
        return findAttendanceHistoryByDate(workDate, userName);
    }

    /**
     * 사용자명으로 전체 근태 기록 조회 (최신순)
     *
     * @param userName 사용자명
     * @return 근태 기록 목록 (최신순)
     */
    private List<AttendanceHistoryDTO> findAttendanceHistoryByUserName(String userName) {
        // 1. 사용자명으로 근태 기록 조회 (최신순)
        List<UserAttendance> attendanceList = userAttendanceRepository
            .findByUserNameContaining(userName);

        // 2. 각 근태 기록의 날짜별 휴가 상태 조회 (날짜 목록 추출)
        List<LocalDate> workDates = attendanceList.stream()
            .map(UserAttendance::getWorkDate)
            .distinct()
            .collect(Collectors.toList());

        // 3. 해당 날짜들의 승인된 휴가 요청 조회
        Map<String, Map<LocalDate, String>> leaveStatusMap = new HashMap<>();
        if (!workDates.isEmpty()) {
            LocalDate minDate = workDates.stream().min(LocalDate::compareTo).orElse(null);
            LocalDate maxDate = workDates.stream().max(LocalDate::compareTo).orElse(null);

            if (minDate != null && maxDate != null) {
                List<UserLeaveRequest> approvedLeaveRequests = userLeaveRequestRepository
                    .findAllByDateRange(minDate, maxDate);

                // userId -> (workDate -> leaveStatus) 맵 생성
                for (UserLeaveRequest lr : approvedLeaveRequests) {
                    if (lr.getStatus() == UserLeaveRequest.RequestStatus.APPROVE) {
                        String userId = lr.getUser().getId();
                        LocalDate requestDate = lr.getRequestDate();
                        String leaveType = convertLeaveTypeToKorean(lr.getLeaveType());

                        leaveStatusMap
                            .computeIfAbsent(userId, k -> new HashMap<>())
                            .put(requestDate, leaveType);
                    }
                }
            }
        }

        // 4. DTO 변환
        return attendanceList.stream()
            .map(attendance -> {
                AttendanceHistoryDTO dto = AttendanceHistoryDTO.builder()
                    .userId(attendance.getUser().getId())
                    .userName(attendance.getUser().getName())
                    .workDate(attendance.getWorkDate())
                    .checkInTime(attendance.getCheckInTime())
                    .checkOutTime(attendance.getCheckOutTime())
                    .build();

                // 근무시간 계산
                if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
                    Duration duration = Duration.between(
                        attendance.getCheckInTime(),
                        attendance.getCheckOutTime()
                    );
                    double hours = duration.toMinutes() / 60.0;
                    dto.setWorkHours(Math.round(hours * 10.0) / 10.0);
                }

                // 휴가 상태 설정
                String leaveStatus = leaveStatusMap
                    .getOrDefault(attendance.getUser().getId(), new HashMap<>())
                    .get(attendance.getWorkDate());
                if (leaveStatus != null) {
                    dto.setLeaveStatus(leaveStatus);
                }

                return dto;
            })
            .collect(Collectors.toList());
    }

    /**
     * 특정 날짜의 사용자 근태 기록 조회
     *
     * @param workDate 근무일자
     * @param userName 사용자명 (선택)
     * @return 근태 기록 목록
     */
    private List<AttendanceHistoryDTO> findAttendanceHistoryByDate(LocalDate workDate, String userName) {
        // 1. 모든 사용자 조회 (사용자명 필터링)
        List<Users> userList;
        if (userName != null && !userName.trim().isEmpty()) {
            userList = userRepository.findByNameContaining(userName.trim());
        } else {
            userList = userRepository.findAll();
        }

        // 2. 해당 날짜의 승인된 휴가 요청 조회 (한 번의 쿼리로 조회)
        List<UserLeaveRequest> approvedLeaveRequests = userLeaveRequestRepository
            .findByRequestDateAndStatus(workDate, UserLeaveRequest.RequestStatus.APPROVE);

        // 3. 사용자별 휴가 상태 맵 생성
        Map<String, String> leaveStatusMap = approvedLeaveRequests.stream()
            .collect(Collectors.toMap(
                lr -> lr.getUser().getId(),
                lr -> convertLeaveTypeToKorean(lr.getLeaveType()),
                (existing, replacement) -> existing  // 중복 시 첫 번째 값 유지
            ));

        // 4. 각 사용자에 대해 출퇴근 기록 및 휴가 상태 조합
        List<AttendanceHistoryDTO> resultList = new ArrayList<>();

        for (Users user : userList) {
            Optional<UserAttendance> attendanceOpt = userAttendanceRepository
                .findAttendanceByDate(user.getId(), workDate);

            AttendanceHistoryDTO historyDTO = AttendanceHistoryDTO.builder()
                .userId(user.getId())
                .userName(user.getName())
                .workDate(workDate)
                .build();

            if (attendanceOpt.isPresent()) {
                UserAttendance attendance = attendanceOpt.get();
                historyDTO.setCheckInTime(attendance.getCheckInTime());
                historyDTO.setCheckOutTime(attendance.getCheckOutTime());

                // 근무시간 계산 (출퇴근 모두 있는 경우)
                if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
                    Duration duration = Duration.between(
                        attendance.getCheckInTime(),
                        attendance.getCheckOutTime()
                    );
                    double hours = duration.toMinutes() / 60.0;
                    historyDTO.setWorkHours(Math.round(hours * 10.0) / 10.0); // 소수점 첫째 자리까지
                }
            }

            // 휴가 상태 설정
            String leaveStatus = leaveStatusMap.get(user.getId());
            if (leaveStatus != null) {
                historyDTO.setLeaveStatus(leaveStatus);
            }

            resultList.add(historyDTO);
        }

        // 5. 사용자명 정렬
        return resultList.stream()
            .sorted((a, b) -> a.getUserName().compareTo(b.getUserName()))
            .collect(Collectors.toList());
    }

    /**
     * 휴가 유형을 한글로 변환
     */
    private String convertLeaveTypeToKorean(UserLeaveRequest.LeaveType leaveType) {
        switch (leaveType) {
            case ANNUAL_LEAVE:
                return "연차";
            case BUSINESS_TRIP:
                return "출장";
            case FIELD_WORK:
                return "외근";
            default:
                return null;
        }
    }
}
