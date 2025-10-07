package io.github.mskim.comm.cms.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
public class JoinDTO {

    @NotBlank(message = "로그인 아이디는 필수입니다")
    @Size(min = 4, max = 20, message = "로그인 아이디는 4자 이상 20자 이하로 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "로그인 아이디는 영문자와 숫자만 사용 가능합니다")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하로 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
             message = "비밀번호는 영문자, 숫자, 특수문자를 모두 포함해야 합니다")
    private String password;

    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요")
    private String name;

    @NotBlank(message = "역할은 필수입니다")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "역할은 ADMIN 또는 USER만 가능합니다")
    private String role;

    @Size(max = 20, message = "직급은 20자 이하로 입력해주세요")
    private String rank;
}
