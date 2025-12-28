package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.dto.UserUpdateDTO;
import io.github.mskim.comm.cms.entity.Users;

import java.util.List;

public interface UserService {

    Users findByLoginId(String loginId);

    /**
     * 모든 사용자 목록 조회 (관리자 전용)
     *
     * @return 사용자 목록
     */
    List<Users> findAllUsers();

    /**
     * 사용자 정보 수정 (관리자 전용)
     *
     * @param updateDTO 수정할 사용자 정보
     * @return 수정된 사용자 정보
     */
    Users updateUser(UserUpdateDTO updateDTO);

}
