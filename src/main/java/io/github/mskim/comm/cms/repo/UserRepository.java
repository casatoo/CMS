package io.github.mskim.comm.cms.repo;

import io.github.mskim.comm.cms.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {

    boolean existsByLoginId(String loginId);

    Optional<Users> findByLoginId(String loginId);

}
