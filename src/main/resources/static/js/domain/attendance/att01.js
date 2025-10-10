const apiUri = "/api/v1/attendance"
let calendar;
let selectedDate = "";

$(document).ready(function() {
    initCalendar();
    initDateModal();
    onClickAttendanceChangeRequestBtn();
});

let initCalendar = () => {
    // apps.js의 공통 함수 사용
    calendar = initFullCalendar('attendanceCalendar', {
        events: function(fetchInfo, successCallback, failureCallback) {
            let startDate = fetchInfo.startStr.substring(0,10);
            let endDate = fetchInfo.endStr.substring(0,10);

            $.get(apiUri + "/month/all", { startDate: startDate, endDate: endDate }, function (res) {
                if(res.error) {
                    failureCallback();
                } else {
                    const events = [];
                    _.forEach(res, item => {
                        let checkInItem = {
                            title: '출근',
                            start: item.checkInTime,
                            color: item.color || '#00e7ff'
                        }
                        events.push(checkInItem);
                        if (!_.isNull(item.checkOutTime)) {
                            let checkOutItem = {
                                title: '퇴근',
                                start: item.checkOutTime,
                                color: item.color || '#f7ff00'
                            }
                            events.push(checkOutItem);
                        }
                    });
                    successCallback(events);
                }
            });
        },
        dateClick: function(info) {
            const clickedDate = info.dateStr;
            selectedDate = clickedDate;

            // apps.js의 공통 함수 사용 - 날짜 비교
            const comparison = compareDateWithToday(clickedDate);

            // 오늘보다 같거나 미래 날짜는 신청 불가
            if (comparison >= 0) {
                customAlert("출퇴근조정신청", "오늘보다 이전 날짜만 신청할 수 있습니다.", "warning");
                return;
            }

            $("#dateFormTitle").html(`<p>선택한 날짜: ${info.dateStr}</p>`);

            const eventsOnDate = calendar.getEvents().filter(event => {
                const eventDateStr = event.start.toISOString().substring(0, 10);
                return eventDateStr === clickedDate;
            });

            let checkInTime = '';
            let checkOutTime = '';

            eventsOnDate.forEach(event => {
                const timeStr = event.start.toTimeString().substring(0, 5);
                if (event.title.includes('출근')) {
                    checkInTime = timeStr;
                } else if (event.title.includes('퇴근')) {
                    checkOutTime = timeStr;
                }
            });

            initTimePicker('startTime', checkInTime);
            initTimePicker('endTime', checkOutTime);

            $('#dateModal').modal('show');
        }
    });
}