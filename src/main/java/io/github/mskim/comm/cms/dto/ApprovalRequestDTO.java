package io.github.mskim.comm.cms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestDTO {

    @NotBlank(message = "요청 ID는 필수입니다")
    private String requestId;

    private String rejectReason;  // 반려 시에만 사용
}
