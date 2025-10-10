package io.github.mskim.comm.cms.controller.api;

import io.github.mskim.comm.cms.api.ApiPaths;
import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import io.github.mskim.comm.cms.dto.UserProfileDTO;
import io.github.mskim.comm.cms.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.BASE_API + "/profile")
public class ProfileApiController {

    private final UserProfileService userProfileService;

    @Value("${file.upload.path:uploads/profiles}")
    private String uploadPath;

    /**
     * 내 프로필 조회
     */
    @GetMapping("/me")
    public UserProfileDTO getMyProfile() {
        return userProfileService.getMyProfile();
    }

    /**
     * 사용자 정보 수정
     */
    @PutMapping("/update")
    public ApiResponse updateUserInfo(@RequestBody UserProfileDTO profileDTO) {
        try {
            userProfileService.updateUserInfo(profileDTO.getUserId(), profileDTO);
            return ApiResponse.of(ApiStatus.OK, "프로필이 수정되었습니다.");
        } catch (Exception e) {
            log.error("프로필 수정 실패", e);
            return ApiResponse.of(ApiStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 프로필 이미지 업로드
     */
    @PostMapping("/image/upload")
    public ApiResponse uploadProfileImage(@RequestParam("userId") String userId,
                                          @RequestParam("file") MultipartFile file) {
        try {
            userProfileService.uploadProfileImage(userId, file);
            return ApiResponse.of(ApiStatus.OK, "프로필 이미지가 업로드되었습니다.");
        } catch (Exception e) {
            log.error("프로필 이미지 업로드 실패", e);
            return ApiResponse.of(ApiStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 프로필 이미지 삭제
     */
    @DeleteMapping("/image/delete")
    public ApiResponse deleteProfileImage(@RequestParam("userId") String userId) {
        try {
            userProfileService.deleteProfileImage(userId);
            return ApiResponse.of(ApiStatus.OK, "프로필 이미지가 삭제되었습니다.");
        } catch (Exception e) {
            log.error("프로필 이미지 삭제 실패", e);
            return ApiResponse.of(ApiStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 프로필 이미지 조회
     */
    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable("filename") String filename) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // 기본값, 실제로는 DB의 contentType 사용 권장
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("프로필 이미지 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
