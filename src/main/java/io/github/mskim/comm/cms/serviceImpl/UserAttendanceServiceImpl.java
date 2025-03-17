package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.config.CustomModelMapper;
import io.github.mskim.comm.cms.dto.UserAttendanceDTO;
import io.github.mskim.comm.cms.entity.UserAttendance;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.mapper.UserAttendanceMapper;
import io.github.mskim.comm.cms.repository.UserAttendanceRepository;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import io.github.mskim.comm.cms.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserAttendanceServiceImpl implements UserAttendanceService {

    private final UserAttendanceRepository userAttendanceRepository;
    private final UserAttendanceMapper userAttendanceMapper;
    private final UserService userService;

    public UserAttendanceServiceImpl(UserAttendanceRepository userAttendanceRepository, UserAttendanceMapper userAttendanceMapper, UserService userService) {
        this.userAttendanceRepository = userAttendanceRepository;
        this.userAttendanceMapper = userAttendanceMapper;
        this.userService = userService;
    }

    @Override
    public UserAttendanceDTO findTodayCheckInTime(String userId) {
        UserAttendance userAttendance = userAttendanceMapper.findTodayCheckInTime(userId);
        return userAttendance != null ? CustomModelMapper.getInstance().map(userAttendance, UserAttendanceDTO.class) : null;
    }

    @Override
    public int countWorkDaysThisMonth(String userId) {
        return userAttendanceMapper.countWorkDaysThisMonth(userId);
    }

    @Override
    public void checkIn() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        Users user = userService.findByLoginId(loginId);

        UserAttendance userAttendance = UserAttendance.builder()
                .user(user)
                .workDate(LocalDate.now())
                .checkInTime(LocalDateTime.now())
                .build();

        userAttendanceRepository.save(userAttendance); // 사용자 출근 정보 저장
    }

    @Override
    public void checkOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        Users user = userService.findByLoginId(loginId);

        UserAttendance userAttendance = userAttendanceMapper.findTodayCheckInTime(user.getId());

        if (userAttendance != null) {
            userAttendance.setUser(user);
            userAttendance.setCheckOutTime(LocalDateTime.now());

            userAttendanceRepository.save(userAttendance);
        } else {
            throw new IllegalArgumentException("오늘 출근 기록이 없습니다.");
        }
    }
}
