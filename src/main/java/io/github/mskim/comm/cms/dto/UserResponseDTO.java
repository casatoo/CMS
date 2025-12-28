package io.github.mskim.comm.cms.dto;

import io.github.mskim.comm.cms.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 응답 DTO
 *
 * <p>비밀번호를 제외한 사용자 정보를 반환합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String id;
    private String loginId;
    private String name;
    private Double annualLeaveDays;
    private String role;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Users 엔티티를 UserResponseDTO로 변환
     *
     * @param user Users 엔티티
     * @return UserResponseDTO
     */
    public static UserResponseDTO from(Users user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .annualLeaveDays(user.getAnnualLeaveDays())
                .role(user.getRole())
                .position(user.getPosition())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
