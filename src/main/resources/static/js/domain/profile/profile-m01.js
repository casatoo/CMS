$(document).ready(function() {
    let currentProfileData = {};

    // 모달이 열릴 때 프로필 데이터 로드
    $('#profileModal').on('show.bs.modal', function() {
        loadProfile();
    });

    // 모달이 닫힐 때 포커스 관리 (접근성 경고 해결)
    $('#profileModal').on('hide.bs.modal', function(e) {
        // 현재 포커스된 요소가 모달 내부에 있으면 포커스 제거
        const activeElement = document.activeElement;
        if (activeElement && $('#profileModal').find(activeElement).length > 0) {
            activeElement.blur();
        }
    });

    // 이벤트 핸들러 등록
    $('#uploadImageBtn').on('click', function() {
        $('#fileInput').click();
    });
    $('#fileInput').on('change', onFileSelect);
    $('#deleteImageBtn').on('click', onDeleteImage);

    /**
     * 프로필 데이터 로드
     */
    function loadProfile() {
        $.ajax({
            url: '/api/v1/profile/me',
            method: 'GET',
            success: function(data) {
                currentProfileData = data;
                displayProfile(data);
            },
            error: function(xhr) {
                handleAjaxError(xhr, '프로필을 불러오는데 실패했습니다.');
            }
        });
    }

    /**
     * 프로필 데이터 표시
     */
    function displayProfile(data) {
        $('#userId').val(data.userId);
        $('#loginId').text(data.loginId || '-');
        $('#name').text(data.name || '-');
        $('#position').text(data.position || '-');
        $('#annualLeaveDays').text(data.annualLeaveDays || '0');
        $('#role').text(getRoleText(data.role));

        // 프로필 이미지 표시
        if (data.profileImageName) {
            $('#profileModalImage').attr('src', '/api/v1/profile/image/' + data.profileImageName);
            $('#deleteImageBtn').show();
        } else {
            $('#profileModalImage').attr('src', '/svg/solid/bxs-user.svg');
            $('#deleteImageBtn').hide();
        }
    }

    /**
     * 권한 텍스트 변환
     */
    function getRoleText(role) {
        const roleMap = {
            'ROLE_ADMIN': '관리자',
            'ROLE_USER': '사용자',
            'ROLE_MANAGER': '매니저'
        };
        return roleMap[role] || role;
    }

    /**
     * 파일 선택 시
     */
    function onFileSelect(e) {
        const file = e.target.files[0];
        if (!file) return;

        // 파일 유효성 검사
        if (!file.type.startsWith('image/')) {
            Swal.fire({
                icon: 'error',
                title: '파일 형식 오류',
                text: '이미지 파일만 업로드 가능합니다.',
                confirmButtonText: '확인'
            });
            $('#fileInput').val('');
            return;
        }

        // 파일 크기 검사 (5MB)
        const maxSize = 5 * 1024 * 1024;
        if (file.size > maxSize) {
            Swal.fire({
                icon: 'error',
                title: '파일 크기 초과',
                text: '파일 크기는 5MB를 초과할 수 없습니다.',
                confirmButtonText: '확인'
            });
            $('#fileInput').val('');
            return;
        }

        // 미리보기
        const reader = new FileReader();
        reader.onload = function(event) {
            $('#profileModalImage').attr('src', event.target.result);
        };
        reader.readAsDataURL(file);

        // 업로드 확인
        Swal.fire({
            icon: 'question',
            title: '프로필 이미지 업로드',
            text: '이 이미지를 프로필 사진으로 설정하시겠습니까?',
            showCancelButton: true,
            confirmButtonText: '업로드',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                uploadProfileImage(file);
            } else {
                // 취소 시 원래 이미지로 복원
                if (currentProfileData.profileImageName) {
                    $('#profileModalImage').attr('src', '/api/v1/profile/image/' + currentProfileData.profileImageName);
                } else {
                    $('#profileModalImage').attr('src', '/svg/solid/bxs-user.svg');
                }
                $('#fileInput').val('');
            }
        });
    }

    /**
     * 프로필 이미지 업로드
     */
    function uploadProfileImage(file) {
        const formData = new FormData();
        formData.append('userId', $('#userId').val());
        formData.append('file', file);

        $.ajax({
            url: '/api/v1/profile/image/upload',
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                Swal.fire({
                    icon: 'success',
                    title: '업로드 완료',
                    text: response.message || '프로필 이미지가 업로드되었습니다.',
                    confirmButtonText: '확인'
                }).then(() => {
                    // 모달의 프로필 데이터 다시 로드
                    loadProfile();
                    // 헤더의 프로필 이미지도 다시 로드
                    loadHeaderProfileImage();
                    $('#fileInput').val('');
                });
            },
            error: function(xhr) {
                handleAjaxError(xhr, '이미지 업로드에 실패했습니다.');
                // 실패 시 원래 이미지로 복원
                if (currentProfileData.profileImageName) {
                    $('#profileModalImage').attr('src', '/api/v1/profile/image/' + currentProfileData.profileImageName);
                } else {
                    $('#profileModalImage').attr('src', '/svg/solid/bxs-user.svg');
                }
                $('#fileInput').val('');
            }
        });
    }

    /**
     * 프로필 이미지 삭제
     */
    function onDeleteImage() {
        if (!currentProfileData.profileImageName) {
            Swal.fire({
                icon: 'info',
                title: '알림',
                text: '삭제할 프로필 이미지가 없습니다.',
                confirmButtonText: '확인'
            });
            return;
        }

        Swal.fire({
            icon: 'warning',
            title: '프로필 이미지 삭제',
            text: '프로필 이미지를 삭제하시겠습니까?',
            showCancelButton: true,
            confirmButtonText: '삭제',
            cancelButtonText: '취소',
            confirmButtonColor: '#dc3545'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: '/api/v1/profile/image/delete',
                    method: 'DELETE',
                    data: { userId: $('#userId').val() },
                    success: function(response) {
                        Swal.fire({
                            icon: 'success',
                            title: '삭제 완료',
                            text: response.message || '프로필 이미지가 삭제되었습니다.',
                            confirmButtonText: '확인'
                        }).then(() => {
                            loadProfile();
                            // 헤더의 프로필 이미지도 기본 이미지로 변경
                            loadHeaderProfileImage();
                        });
                    },
                    error: function(xhr) {
                        handleAjaxError(xhr, '이미지 삭제에 실패했습니다.');
                    }
                });
            }
        });
    }

    /**
     * 헤더의 프로필 이미지 다시 로드
     */
    function loadHeaderProfileImage() {
        $.ajax({
            url: '/api/v1/profile/me',
            method: 'GET',
            success: function(data) {
                if (data && data.profileImageName) {
                    $('#profileImage').attr('src', '/api/v1/profile/image/' + data.profileImageName);
                } else {
                    $('#profileImage').attr('src', '/svg/solid/bxs-user.svg');
                }
            },
            error: function() {
                console.log('헤더 프로필 이미지를 불러올 수 없습니다.');
            }
        });
    }
});
