package io.github.mskim.comm.cms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_PROFILE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @ToString.Exclude
    private Users user;

    @Column(name = "profile_image_path")
    private String profileImagePath; // 프로필 이미지 파일 경로

    @Column(name = "profile_image_name")
    private String profileImageName; // 프로필 이미지 파일명

    @Column(name = "original_filename")
    private String originalFilename; // 원본 파일명

    @Column(name = "file_size")
    private Long fileSize; // 파일 크기 (bytes)

    @Column(name = "content_type")
    private String contentType; // MIME 타입 (image/jpeg, image/png 등)
}
