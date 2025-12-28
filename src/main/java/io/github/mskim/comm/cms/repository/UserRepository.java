package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.Users;

import java.util.List;
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

    /**
     * 이름으로 사용자 검색
     *
     * @param name 사용자명 (부분 일치)
     * @return 검색된 사용자 목록
     */
    List<Users> findByNameContaining(String name);

}
