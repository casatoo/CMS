package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.Users;

import java.util.Optional;

/**
 * 사용자 Repository
 *
 * @author CMS Team
 * @since 1.0.0
 */
public interface UserRepository extends BaseRepository<Users, String> {

    boolean existsByLoginId(String loginId);

    Optional<Users> findByLoginId(String loginId);

}
