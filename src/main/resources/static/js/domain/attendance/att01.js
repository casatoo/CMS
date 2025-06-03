const apiUri = "/api/v1/attendance"
let calendar;
let selectedDate = "";

$(document).ready(function() {
    initCalendar();
    initDateModal();
    onClickAttendanceChangeRequestBtn();
});

let initCalendar = () => {
    let calendarEl = $('#attendanceCalendar')[0];

    calendar = new FullCalendar.Calendar(calendarEl, {
        locale: 'ko',
        initialView: 'dayGridMonth',
        aspectRatio: 1.8,
        headerToolbar: {
            left: 'today',
            center: 'title',
            right: 'prev,next'
        },
        buttonText: {
            today: '이번달'
        },
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
            const clicked = new Date(clickedDate);
            const today = new Date();

            selectedDate = clickedDate;

            clicked.setHours(0, 0, 0, 0);
            today.setHours(0, 0, 0, 0);

            // 오늘보다 같거나 미래 날짜는 신청 불가
            if (clicked >= today) {
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
        },
        eventDidMount: function(info) {
            $(info.el).closest('.fc-daygrid-day').css({
                height: '120px'
            });
            $(info.el).find('.fc-event-title').css('color', 'black');
            $(info.el).find('.fc-event-time').css('color', 'black');
        },
        datesSet: function() {
            $('.fc-col-header-cell a').css({
                'color': 'black',
                'text-decoration': 'none'
            });
            $('.fc-daygrid-day-number').css({
                'color': 'black',
                'text-decoration': 'none'
            });
        }
    });
    calendar.render();
}