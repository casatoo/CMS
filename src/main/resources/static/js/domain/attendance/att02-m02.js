/**
 * 연차/외근/출장 상세정보 모달
 */

let initLeaveDetailModal = function () {
    $('#leaveDetailModal').on('hide.bs.modal', function (e) {
        // 모달 내부에 포커스가 있는 요소의 포커스 제거
        const focusedElement = $(this).find(':focus');
        if (focusedElement.length > 0) {
            focusedElement.blur();
        }
        clearLeaveDetailModal();
    });

    // 모달이 완전히 숨겨진 후 포커스 정리
    $('#leaveDetailModal').on('hidden.bs.modal', function () {
        $(document.activeElement).blur();
    });
}

let showLeaveDetailModal = function (event) {
    const props = event.extendedProps;

    // 상태 표시
    let statusText = '';
    let statusColor = '';
    switch(props.status) {
        case 'REQUEST':
            statusText = '신청중';
            statusColor = '#007bff';
            break;
        case 'APPROVE':
            statusText = '승인됨';
            statusColor = '#28a745';
            break;
        case 'REJECT':
            statusText = '반려됨';
            statusColor = '#dc3545';
            break;
        default:
            statusText = props.status;
            statusColor = '#6c757d';
    }
    $('#detailStatus').html(`<span style="background-color: ${statusColor}; color: white; padding: 4px 12px; border-radius: 4px; font-size: 14px;">${statusText}</span>`);

    // 신청자
    $('#detailUserName').text(props.userName || '-');

    // 신청 날짜
    $('#detailRequestDate').text(event.startStr || '-');

    // 신청 유형
    let leaveTypeText = '';
    switch(props.leaveType) {
        case 'ANNUAL_LEAVE':
            leaveTypeText = '연차';
            break;
        case 'BUSINESS_TRIP':
            leaveTypeText = '출장';
            break;
        case 'FIELD_WORK':
            leaveTypeText = '외근';
            break;
        default:
            leaveTypeText = props.leaveType;
    }
    $('#detailLeaveType').text(leaveTypeText);

    // 기간 유형
    let periodTypeText = '';
    switch(props.periodType) {
        case 'ALL_DAY':
            periodTypeText = '종일';
            break;
        case 'MORNING':
            periodTypeText = '오전';
            break;
        case 'AFTERNOON':
            periodTypeText = '오후';
            break;
        default:
            periodTypeText = props.periodType;
    }
    $('#detailPeriodType').text(periodTypeText);

    // 장소 (외근/출장인 경우에만 표시)
    if (props.leaveType === 'FIELD_WORK' || props.leaveType === 'BUSINESS_TRIP') {
        $('#detailLocation').text(props.location || '-');
        $('#detailLocationGroup').show();
    } else {
        $('#detailLocationGroup').hide();
    }

    // 신청사유
    $('#detailReason').text(props.reason || '-');

    // 모달 표시
    $('#leaveDetailModal').modal('show');
}

let clearLeaveDetailModal = function () {
    $('#detailStatus').html('');
    $('#detailUserName').text('');
    $('#detailRequestDate').text('');
    $('#detailLeaveType').text('');
    $('#detailPeriodType').text('');
    $('#detailLocation').text('');
    $('#detailReason').text('');
    $('#detailLocationGroup').hide();
}
