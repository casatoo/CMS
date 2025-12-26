package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserProfile;
import io.github.mskim.comm.cms.entity.Users;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 프로필 Repository
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Repository
public interface UserProfileRepository extends BaseRepository<UserProfile, String> {

    Optional<UserProfile> findByUser(Users user);

    Optional<UserProfile> findByUserId(String userId);

    void deleteByUserId(String userId);
}
