let currentUserData = null;
const userDetailModal = new bootstrap.Modal(document.getElementById('userDetailModal'));

// 모달 초기화 함수
function initUserDetailModal() {
    // 저장 버튼 클릭
    $('#btnSaveUser').off('click').on('click', function() {
        saveUserInfo();
    });

    // 모달 닫힐 때 포커스 정리
    $('#userDetailModal').on('hide.bs.modal', function () {
        const focusedElement = $(this).find(':focus');
        if (focusedElement.length > 0) {
            focusedElement.blur();
        }
    });

    $('#userDetailModal').on('hidden.bs.modal', function () {
        $(document.activeElement).blur();
    });

    // 연차 입력 유효성 검사
    $('#detailAnnualLeaveDays').on('input', function() {
        const value = parseFloat($(this).val());
        if (value < 0) {
            $(this).val(0);
        } else if (value > 100) {
            $(this).val(100);
        }
    });
}

// 모달 열기
function openUserDetailModal(userData) {
    currentUserData = userData;

    // 읽기 전용 정보 표시
    $('#userId').val(userData.id);
    $('#detailLoginId').text(userData.loginId || '-');
    $('#detailName').text(userData.name || '-');
    $('#detailCreatedAt').text(formatDateTime(userData.createdAt));
    $('#detailUpdatedAt').text(formatDateTime(userData.updatedAt));

    // 수정 가능한 정보 설정
    $('#detailPosition').val(userData.position || '');
    $('#detailRole').val(userData.role || 'ROLE_USER');
    $('#detailAnnualLeaveDays').val(userData.annualLeaveDays || 0);

    // 모달 열기
    userDetailModal.show();
}

// 사용자 정보 저장
function saveUserInfo() {
    if (!currentUserData) return;

    // 폼 데이터 수집
    const position = $('#detailPosition').val().trim();
    const role = $('#detailRole').val();
    const annualLeaveDays = parseFloat($('#detailAnnualLeaveDays').val());

    // 유효성 검사
    if (!position) {
        Swal.fire({
            title: '입력 오류',
            text: '직급을 입력해주세요.',
            icon: 'warning',
            confirmButtonText: '확인'
        });
        $('#detailPosition').focus();
        return;
    }

    if (!role) {
        Swal.fire({
            title: '입력 오류',
            text: '권한을 선택해주세요.',
            icon: 'warning',
            confirmButtonText: '확인'
        });
        $('#detailRole').focus();
        return;
    }

    if (isNaN(annualLeaveDays) || annualLeaveDays < 0) {
        Swal.fire({
            title: '입력 오류',
            text: '올바른 연차 일수를 입력해주세요.',
            icon: 'warning',
            confirmButtonText: '확인'
        });
        $('#detailAnnualLeaveDays').focus();
        return;
    }

    // 변경 사항 확인
    const hasChanges =
        currentUserData.position !== position ||
        currentUserData.role !== role ||
        currentUserData.annualLeaveDays !== annualLeaveDays;

    if (!hasChanges) {
        Swal.fire({
            title: '변경 사항 없음',
            text: '수정된 내용이 없습니다.',
            icon: 'info',
            confirmButtonText: '확인'
        });
        return;
    }

    // 저장 확인
    Swal.fire({
        title: '사용자 정보 수정',
        html: `<strong>${currentUserData.name}</strong> 님의 정보를 수정하시겠습니까?<br><br>` +
              `<div class="text-start">` +
              `<small class="text-muted">` +
              `직급: ${currentUserData.position || '-'} → <strong>${position}</strong><br>` +
              `권한: ${formatRole(currentUserData.role)} → <strong>${formatRole(role)}</strong><br>` +
              `남은 연차: ${currentUserData.annualLeaveDays || 0}일 → <strong>${annualLeaveDays}일</strong>` +
              `</small></div>`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#0d6efd',
        cancelButtonColor: '#6c757d',
        confirmButtonText: '저장',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            const updateData = {
                id: currentUserData.id,
                position: position,
                role: role,
                annualLeaveDays: annualLeaveDays
            };

            // apps.js의 postRequest 사용
            postRequest(
                '/api/v1/user/update',
                updateData,
                function(response) {
                    Swal.fire({
                        title: '저장 완료',
                        text: '사용자 정보가 수정되었습니다.',
                        icon: 'success',
                        confirmButtonText: '확인'
                    }).then(() => {
                        userDetailModal.hide();
                        // 그리드 새로고침
                        if (typeof loadUsers === 'function') {
                            loadUsers({});
                        }
                    });
                },
                function(xhr) {
                    const errorMessage = handleAjaxError(xhr, '사용자 정보 수정에 실패했습니다.');
                    Swal.fire({
                        title: '저장 실패',
                        text: errorMessage,
                        icon: 'error',
                        confirmButtonText: '확인'
                    });
                }
            );
        }
    });
}

// 날짜/시간 포맷 함수
function formatDateTime(dateTime) {
    if (!dateTime) return '-';
    if (typeof dateTime === 'string') {
        return dateTime.replace('T', ' ').substring(0, 19);
    }
    return dateTime;
}

// 권한 포맷
function formatRole(role) {
    if (!role) return '-';
    switch (role) {
        case 'ROLE_ADMIN': return '관리자';
        case 'ROLE_USER': return '사용자';
        default: return role;
    }
}

// 문서 로드 시 초기화
$(document).ready(function() {
    initUserDetailModal();
});
