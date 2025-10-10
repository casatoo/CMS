package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserProfile;
import io.github.mskim.comm.cms.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    Optional<UserProfile> findByUser(Users user);

    Optional<UserProfile> findByUserId(String userId);

    void deleteByUserId(String userId);
}
