package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserProfile;
import io.github.mskim.comm.cms.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * 사용자 엔티티로 프로필 조회 (N+1 방지)
     *
     * @param user 사용자 엔티티
     * @return 사용자 프로필
     */
    @Query("""
        SELECT up FROM UserProfile up
        JOIN FETCH up.user u
        WHERE u = :user
    """)
    Optional<UserProfile> findByUser(@Param("user") Users user);

    /**
     * 사용자 ID로 프로필 조회 (N+1 방지)
     *
     * @param userId 사용자 ID
     * @return 사용자 프로필
     */
    @Query("""
        SELECT up FROM UserProfile up
        JOIN FETCH up.user u
        WHERE u.id = :userId
    """)
    Optional<UserProfile> findByUserId(@Param("userId") String userId);

    void deleteByUserId(String userId);
}
