package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.repository.UserAttendanceRepository;
import io.github.mskim.comm.cms.service.UserAttendanceService;
import org.springframework.stereotype.Service;

@Service
public class UserAttendanceServiceImpl implements UserAttendanceService {

    private final UserAttendanceRepository userAttendanceRepository;

    public UserAttendanceServiceImpl(UserAttendanceRepository userAttendanceRepository) {
        this.userAttendanceRepository = userAttendanceRepository;
    }

}
