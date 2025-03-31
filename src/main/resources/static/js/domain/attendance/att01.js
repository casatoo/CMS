const apiUri = "/api/v1/attendance"

$(document).ready(function() {
    initCalendar();
});

var initCalendar = () => {
    var calendarEl = $('#attendanceCalendar')[0];

    var calendar = new FullCalendar.Calendar(calendarEl, {
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
                            title: ' 출근',
                            start: item.checkInTime,
                            color: item.color || '#00e7ff'
                        }
                        events.push(checkInItem);
                        if (!_.isNull(item.checkOutTime)) {
                            let checkOutItem = {
                                title: ' 퇴근',
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