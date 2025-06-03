package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAttendanceChangeRequestRepository extends JpaRepository<UserAttendanceChangeRequest, String> {

}
