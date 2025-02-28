package io.github.mskim.comm.cms.repo;

import io.github.mskim.comm.cms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByLoginId(String loginId);

    Optional<UserEntity> findByLoginId(String loginId);

}
