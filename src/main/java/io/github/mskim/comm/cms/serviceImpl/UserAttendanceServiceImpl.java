package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.config.CustomModelMapper;
import io.github.mskim.comm.cms.dto.UserAttendanceDTO;
import io.github.mskim.comm.cms.entity.UserAttendance;
import io.github.mskim.comm.cms.mapper.UserAttendanceMapper;
import io.github.mskim.comm.cms.repository.UserAttendanceRepository;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import org.springframework.stereotype.Service;

@Service
public class UserAttendanceServiceImpl implements UserAttendanceService {

    private final UserAttendanceRepository userAttendanceRepository;
    private final UserAttendanceMapper userAttendanceMapper;

    public UserAttendanceServiceImpl(UserAttendanceRepository userAttendanceRepository, UserAttendanceMapper userAttendanceMapper) {
        this.userAttendanceRepository = userAttendanceRepository;
        this.userAttendanceMapper = userAttendanceMapper;
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

}
