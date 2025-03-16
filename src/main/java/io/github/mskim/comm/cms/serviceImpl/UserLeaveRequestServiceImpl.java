package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.repository.UserLeaveRequestRepository;
import io.github.mskim.comm.cms.service.UserLeaveRequestService;
import org.springframework.stereotype.Service;

@Service
public class UserLeaveRequestServiceImpl implements UserLeaveRequestService {

    private final UserLeaveRequestRepository userLeaveRequestRepository;

    public UserLeaveRequestServiceImpl(UserLeaveRequestRepository userLeaveRequestRepository) {
        this.userLeaveRequestRepository = userLeaveRequestRepository;
    }

}
