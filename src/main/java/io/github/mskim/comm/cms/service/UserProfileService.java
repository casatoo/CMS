package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.dto.UserProfileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {

    /**
     * 사용자 프로필 조회
     */
    UserProfileDTO getUserProfile(String userId);

    /**
     * 현재 로그인한 사용자의 프로필 조회
     */
    UserProfileDTO getMyProfile();

    /**
     * 사용자 정보 수정 (이름, 직급, 연차)
     */
    UserProfileDTO updateUserInfo(String userId, UserProfileDTO profileDTO);

    /**
     * 프로필 이미지 업로드
     */
    UserProfileDTO uploadProfileImage(String userId, MultipartFile file);

    /**
     * 프로필 이미지 삭제
     */
    UserProfileDTO deleteProfileImage(String userId);
}
