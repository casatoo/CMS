let currentRequestData = null;
const leaveRequestModal = new bootstrap.Modal(document.getElementById('leaveRequestDetailModal'));

// 모달 초기화 함수
function initLeaveRequestDetailModal() {
    // 승인 버튼 클릭
    $('#btnApprove').off('click').on('click', function() {
        approveLeaveRequest();
    });

    // 반려 버튼 클릭
    $('#btnReject').off('click').on('click', function() {
        rejectLeaveRequest();
    });
}

// 모달 열기
function openLeaveRequestDetailModal(requestData) {
    currentRequestData = requestData;

    // 기본 정보 표시
    $('#detailUserName').text(requestData.userName || requestData.userLoginId || '-');
    $('#detailLeaveType').text(formatLeaveType(requestData.leaveType));
    $('#detailRequestDate').text(requestData.requestDate || '-');
    $('#detailPeriodType').text(formatPeriodType(requestData.periodType));
    $('#detailLocation').text(requestData.location || '-');
    $('#detailReason').text(requestData.reason || '-');
    $('#detailCreatedAt').text(formatDateTime(requestData.createdAt));

    // 상태 표시
    const statusBadge = $('#detailStatus');
    statusBadge.removeClass('bg-warning bg-success bg-danger');
    switch(requestData.status) {
        case 'REQUEST':
            statusBadge.addClass('bg-warning').text('신청');
            break;
        case 'APPROVE':
            statusBadge.addClass('bg-success').text('승인');
            break;
        case 'REJECT':
            statusBadge.addClass('bg-danger').text('반려');
            break;
        default:
            statusBadge.addClass('bg-secondary').text(requestData.status);
    }

    // 승인자 정보 표시/숨김
    if (requestData.approverName) {
        $('#detailApproverRow').show();
        $('#detailApproverName').text(requestData.approverName + ' (' + (requestData.approverLoginId || '') + ')');
    } else {
        $('#detailApproverRow').hide();
    }

    // 승인일시 표시/숨김
    if (requestData.approvedAt) {
        $('#detailApprovedAtRow').show();
        $('#detailApprovedAt').text(formatDateTime(requestData.approvedAt));
    } else {
        $('#detailApprovedAtRow').hide();
    }

    // 버튼 표시/숨김
    if (requestData.status === 'REQUEST') {
        // 신청 상태일 때만 승인/반려 버튼 표시
        $('#btnApprove').show();
        $('#btnReject').show();
        $('#detailRejectReasonRow').hide();
        $('#detailRejectReason').val('');
    } else {
        // 승인/반려된 상태는 버튼 숨김
        $('#btnApprove').hide();
        $('#btnReject').hide();
        $('#detailRejectReasonRow').hide();
    }

    // 모달 열기
    leaveRequestModal.show();
}

// 승인 처리
function approveLeaveRequest() {
    if (!currentRequestData) return;

    // 연차 차감 안내 메시지
    let confirmMessage = '해당 신청을 승인하시겠습니까?';
    if (currentRequestData.leaveType === 'ANNUAL_LEAVE') {
        const deductDays = currentRequestData.periodType === 'ALL_DAY' ? 1 : 0.5;
        confirmMessage += `\n(연차 ${deductDays}일이 차감됩니다)`;
    }

    Swal.fire({
        title: '신청 승인',
        text: confirmMessage,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#198754',
        cancelButtonColor: '#6c757d',
        confirmButtonText: '승인',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            const requestData = {
                requestId: currentRequestData.id,
                rejectReason: null
            };

            postRequest(
                '/api/v1/leave/request/approve',
                requestData,
                function(response) {
                    Swal.fire({
                        title: '승인 완료',
                        text: '신청이 승인되었습니다.',
                        icon: 'success',
                        confirmButtonText: '확인'
                    }).then(() => {
                        leaveRequestModal.hide();
                        // 그리드 새로고침
                        if (typeof loadLeaveRequests === 'function') {
                            loadLeaveRequests({});
                        }
                    });
                },
                function(xhr) {
                    handleAjaxError(xhr, '신청 승인에 실패했습니다.');
                }
            );
        }
    });
}

// 반려 처리
function rejectLeaveRequest() {
    if (!currentRequestData) return;

    // 반려 사유 입력란 표시
    const rejectReasonRow = $('#detailRejectReasonRow');
    if (rejectReasonRow.is(':visible')) {
        // 이미 표시된 경우 - 반려 처리 진행
        const rejectReason = $('#detailRejectReason').val().trim();

        if (!rejectReason) {
            Swal.fire({
                title: '입력 오류',
                text: '반려 사유를 입력해주세요.',
                icon: 'warning',
                confirmButtonText: '확인'
            });
            return;
        }

        Swal.fire({
            title: '신청 반려',
            text: '해당 신청을 반려하시겠습니까?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#dc3545',
            cancelButtonColor: '#6c757d',
            confirmButtonText: '반려',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                const requestData = {
                    requestId: currentRequestData.id,
                    rejectReason: rejectReason
                };

                postRequest(
                    '/api/v1/leave/request/reject',
                    requestData,
                    function(response) {
                        Swal.fire({
                            title: '반려 완료',
                            text: '신청이 반려되었습니다.',
                            icon: 'success',
                            confirmButtonText: '확인'
                        }).then(() => {
                            leaveRequestModal.hide();
                            // 그리드 새로고침
                            if (typeof loadLeaveRequests === 'function') {
                                loadLeaveRequests({});
                            }
                        });
                    },
                    function(xhr) {
                        handleAjaxError(xhr, '신청 반려에 실패했습니다.');
                    }
                );
            }
        });
    } else {
        // 첫 클릭 - 반려 사유 입력란 표시
        rejectReasonRow.show();
        $('#detailRejectReason').focus();
    }
}

// 날짜/시간 포맷 함수
function formatDateTime(dateTime) {
    if (!dateTime) return '-';
    if (typeof dateTime === 'string') {
        return dateTime.replace('T', ' ').substring(0, 19);
    }
    return dateTime;
}

// 휴가 유형 포맷
function formatLeaveType(leaveType) {
    if (!leaveType) return '-';
    switch (leaveType) {
        case 'ANNUAL_LEAVE': return '연차';
        case 'FIELD_WORK': return '외근';
        case 'BUSINESS_TRIP': return '출장';
        default: return leaveType;
    }
}

// 기간 유형 포맷
function formatPeriodType(periodType) {
    if (!periodType) return '-';
    switch (periodType) {
        case 'ALL_DAY': return '종일';
        case 'MORNING': return '오전';
        case 'AFTERNOON': return '오후';
        default: return periodType;
    }
}

// 문서 로드 시 초기화
$(document).ready(function() {
    initLeaveRequestDetailModal();
});
