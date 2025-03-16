package io.github.mskim.comm.cms.repo;

import io.github.mskim.comm.cms.entity.UserAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAttendanceRepository extends JpaRepository<UserAttendance, String> {

}
