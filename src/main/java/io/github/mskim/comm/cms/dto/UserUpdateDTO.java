package io.github.mskim.comm.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 사용자 정보 수정 DTO
 *
 * <p>관리자가 사용자 정보를 수정할 때 사용하는 DTO입니다.</p>
 * <p>수정 가능한 항목: 직급, 권한, 남은 연차</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    /**
     * 사용자 ID
     */
    @NotBlank(message = "사용자 ID는 필수입니다")
    private String id;

    /**
     * 직급
     */
    @NotBlank(message = "직급은 필수입니다")
    private String position;

    /**
     * 권한 (ROLE_USER, ROLE_ADMIN)
     */
    @NotBlank(message = "권한은 필수입니다")
    private String role;

    /**
     * 남은 연차 일수
     */
    @NotNull(message = "연차 일수는 필수입니다")
    @Min(value = 0, message = "연차 일수는 0 이상이어야 합니다")
    private Double annualLeaveDays;
}
