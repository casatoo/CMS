package io.github.mskim.comm.cms.dto;

import io.github.mskim.comm.cms.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private String userId;
    private String loginId; // 로그인 ID (수정 불가)
    private String name; // 이름
    private String position; // 직급
    private Double annualLeaveDays; // 연차 일수
    private String role; // 권한

    // 프로필 이미지 정보
    private String profileImagePath;
    private String profileImageName;
    private String originalFilename;
    private Long fileSize;
    private String contentType;

    public static UserProfileDTO from(io.github.mskim.comm.cms.entity.Users user, UserProfile profile) {
        UserProfileDTOBuilder builder = UserProfileDTO.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .position(user.getPosition())
                .annualLeaveDays(user.getAnnualLeaveDays())
                .role(user.getRole());

        if (profile != null) {
            builder.profileImagePath(profile.getProfileImagePath())
                    .profileImageName(profile.getProfileImageName())
                    .originalFilename(profile.getOriginalFilename())
                    .fileSize(profile.getFileSize())
                    .contentType(profile.getContentType());
        }

        return builder.build();
    }
}
