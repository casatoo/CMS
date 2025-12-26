package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.dto.UserProfileDTO;
import io.github.mskim.comm.cms.entity.UserProfile;
import io.github.mskim.comm.cms.entity.Users;
import io.github.mskim.comm.cms.repository.UserProfileRepository;
import io.github.mskim.comm.cms.repository.UserRepository;
import io.github.mskim.comm.cms.service.UserProfileService;
import io.github.mskim.comm.cms.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final SecurityContextUtil securityContextUtil;

    @Value("${file.upload.path:uploads/profiles}")
    private String uploadPath;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        UserProfile profile = userProfileRepository.findByUserId(userId).orElse(null);

        return UserProfileDTO.from(user, profile);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getMyProfile() {
        Users user = securityContextUtil.getCurrentUser();

        UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);

        return UserProfileDTO.from(user, profile);
    }

    @Override
    @Transactional
    public UserProfileDTO updateUserInfo(String userId, UserProfileDTO profileDTO) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 이름, 직급, 연차 수정
        user.setName(profileDTO.getName());
        user.setPosition(profileDTO.getPosition());
        user.setAnnualLeaveDays(profileDTO.getAnnualLeaveDays());

        userRepository.save(user);

        UserProfile profile = userProfileRepository.findByUserId(userId).orElse(null);

        return UserProfileDTO.from(user, profile);
    }

    @Override
    @Transactional
    public UserProfileDTO uploadProfileImage(String userId, MultipartFile file) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 파일 유효성 검증
        if (file.isEmpty()) {
            throw new RuntimeException("파일이 비어있습니다.");
        }

        // 이미지 파일만 허용
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("이미지 파일만 업로드 가능합니다.");
        }

        // 파일 크기 제한 (5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("파일 크기는 5MB를 초과할 수 없습니다.");
        }

        try {
            // 업로드 디렉토리 생성
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 고유한 파일명 생성
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String savedFilename = UUID.randomUUID().toString() + extension;
            Path filePath = uploadDir.resolve(savedFilename);

            // 기존 프로필 이미지가 있으면 삭제
            UserProfile existingProfile = userProfileRepository.findByUserId(userId).orElse(null);
            if (existingProfile != null && existingProfile.getProfileImagePath() != null) {
                deleteImageFile(existingProfile.getProfileImagePath());
            }

            // 파일 저장
            file.transferTo(filePath.toFile());

            // UserProfile 저장 또는 업데이트
            UserProfile profile;
            if (existingProfile != null) {
                profile = existingProfile;
            } else {
                profile = UserProfile.builder()
                        .user(user)
                        .build();
            }

            profile.setProfileImagePath(filePath.toString());
            profile.setProfileImageName(savedFilename);
            profile.setOriginalFilename(originalFilename);
            profile.setFileSize(file.getSize());
            profile.setContentType(contentType);

            userProfileRepository.save(profile);

            return UserProfileDTO.from(user, profile);

        } catch (IOException e) {
            log.error("파일 업로드 실패 - 경로: {}, 파일명: {}", uploadPath, file.getOriginalFilename(), e);
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserProfileDTO deleteProfileImage(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("프로필 이미지가 없습니다."));

        // 파일 삭제
        if (profile.getProfileImagePath() != null) {
            deleteImageFile(profile.getProfileImagePath());
        }

        // 프로필 이미지 정보 초기화
        profile.setProfileImagePath(null);
        profile.setProfileImageName(null);
        profile.setOriginalFilename(null);
        profile.setFileSize(null);
        profile.setContentType(null);

        userProfileRepository.save(profile);

        return UserProfileDTO.from(user, profile);
    }

    private void deleteImageFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", filePath, e);
        }
    }
}
