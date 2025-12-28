package io.github.mskim.comm.cms.controller.api;

import io.github.mskim.comm.cms.api.ApiPaths;
import io.github.mskim.comm.cms.dto.UserResponseDTO;
import io.github.mskim.comm.cms.dto.UserUpdateDTO;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 관리 API Controller
 *
 * <p>관리자 전용 사용자 관리 API를 제공합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.USER)
public class UserApiController {

    private final UserService userService;

    /**
     * 전체 사용자 목록 조회 (관리자 전용)
     *
     * @return 사용자 목록 (비밀번호 제외)
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponseDTO> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(UserResponseDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 정보 수정 (관리자 전용)
     *
     * <p>관리자가 사용자의 직급, 권한, 남은 연차를 수정합니다.</p>
     *
     * @param updateDTO 수정할 사용자 정보
     * @return 수정된 사용자 정보 (비밀번호 제외)
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserResponseDTO updateUser(@Valid @RequestBody UserUpdateDTO updateDTO) {
        Users updatedUser = userService.updateUser(updateDTO);
        return UserResponseDTO.from(updatedUser);
    }
}
