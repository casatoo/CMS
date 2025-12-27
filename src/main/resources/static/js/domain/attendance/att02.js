const apiUri = "/api/v1/leave"
let calendar;
let selectedDate = "";

$(document).ready(function() {
    initCalendar();
    initLeaveRequestModal();
    onClickLeaveRequestBtn();
});

let initCalendar = () => {
    // apps.js의 공통 함수 사용
    calendar = initFullCalendar('leavesCalendar', {
        events: function(fetchInfo, successCallback, failureCallback) {
            let startDate = fetchInfo.startStr.substring(0,10);
            let endDate = fetchInfo.endStr.substring(0,10);

            // 연차/출장/외근 신청 조회 (승인된 것과 신청 중인 것)
            $.get(apiUri + "/request/search", {
                requestDateStart: startDate,
                requestDateEnd: endDate
                // status 파라미터 제거 - 모든 상태 조회
            }, function(res) {
                const events = [];

                // 연차/출장/외근 배지 추가
                _.forEach(res, item => {
                    // 반려된 것은 표시하지 않음
                    if (item.status === 'REJECT') {
                        return;
                    }

                    let leaveTypeText = '';
                    let leaveColor = '';

                    // 신청 유형에 따른 텍스트
                    switch(item.leaveType) {
                        case 'ANNUAL_LEAVE':
                            leaveTypeText = '연차';
                            break;
                        case 'BUSINESS_TRIP':
                            leaveTypeText = '출장';
                            break;
                        case 'FIELD_WORK':
                            leaveTypeText = '외근';
                            break;
                    }

                    // 기간 유형 표시 (항상 표시)
                    switch(item.periodType) {
                        case 'ALL_DAY':
                            leaveTypeText += '(종일)';
                            break;
                        case 'MORNING':
                            leaveTypeText += '(오전)';
                            break;
                        case 'AFTERNOON':
                            leaveTypeText += '(오후)';
                            break;
                    }

                    // 상태에 따른 색상 및 텍스트
                    if (item.status === 'REQUEST') {
                        leaveTypeText += '(신청중)';
                        leaveColor = '#007bff';  // 파란색 - 신청중
                    } else if (item.status === 'APPROVE') {
                        leaveColor = '#28a745';  // 녹색 - 승인됨
                    }

                    let leaveItem = {
                        title: leaveTypeText,
                        start: item.requestDate,
                        allDay: true,  // 하루 종일 이벤트로 표시
                        color: leaveColor,
                        extendedProps: {
                            id: item.id,
                            leaveType: item.leaveType,
                            periodType: item.periodType,
                            status: item.status,
                            userName: item.userName,
                            location: item.location,
                            reason: item.reason
                        }
                    };
                    events.push(leaveItem);
                });

                successCallback(events);
            }).fail(function(error) {
                console.error('캘린더 데이터 조회 실패:', error);
                failureCallback();
            });
        },
        dateClick: function(info) {
            const clickedDate = info.dateStr;
            selectedDate = clickedDate;

            // apps.js의 공통 함수 사용 - 날짜 비교
            const comparison = compareDateWithToday(clickedDate);

            // 오늘보다 이전 날짜는 신청 불가
            if (comparison < 0) {
                customAlert("연차/외근신청", "오늘 이후 날짜만 신청할 수 있습니다.", "warning");
                return;
            }

            $("#leaveFormTitle").html(`<p>선택한 날짜: ${info.dateStr}</p>`);
            $('#leaveModal').modal('show');
        }
    });
}