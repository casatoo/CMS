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