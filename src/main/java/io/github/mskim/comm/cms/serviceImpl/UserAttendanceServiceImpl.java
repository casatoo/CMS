package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.config.CustomModelMapper;
import io.github.mskim.comm.cms.sp.UserAttendanceSP;
import io.github.mskim.comm.cms.dto.UserAttendanceDTO;
import io.github.mskim.comm.cms.entity.UserAttendance;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.repository.UserAttendanceRepository;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import io.github.mskim.comm.cms.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

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
    private final SecurityContextUtil securityContextUtil;

    @Override
    public UserAttendanceDTO findTodayCheckInTime(String userId) {
        // JPA Repository 사용 (MyBatis 제거)
        Optional<UserAttendance> userAttendance = userAttendanceRepository
            .findTodayCheckInTime(userId, LocalDate.now());

        return userAttendance
            .map(ua -> CustomModelMapper.getInstance().map(ua, UserAttendanceDTO.class))
            .orElse(null);
    }

    @Override
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

    @Override
    @Transactional
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

    @Override
    @Transactional
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

    @Override
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
}
