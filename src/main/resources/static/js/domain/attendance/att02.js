const apiUri = "/api/v1/attendance"
let calendar;

$(document).ready(function() {
    initCalendar();
    initLeavesRequestModal();
});

let initCalendar = () => {
    let calendarEl = $('#leavesCalendar')[0];

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

            $.get(apiUri + "/leave/all", { startDate: startDate, endDate: endDate }, function (res) {
                if(res.error) {
                    failureCallback();
                } else {
                    const events = [];
                    _.forEach(res, item => {
                        let leaveDay = {
                            title: '연차',
                            start: item.leaveDay,
                            color: item.color || '#00e7ff'
                        }
                        events.push(leaveDay);
                    });
                    successCallback(events);
                }
            });
        },
        dateClick: function(info) {
            // 연차 신청 모달 호출
            $('#leavesRequestModal').modal('show');
        },
        eventDidMount: function(info) {
            $(info.el).closest('.fc-daygrid-day').css({height: '120px'});
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