const apiUri = "/api/v1/attendance"

$(document).ready(function() {
  initCalendar();
});

var initCalendar = () => {
  var calendarEl = $('#calendar')[0];

  var calendar = new FullCalendar.Calendar(calendarEl, {
    locale: 'ko',
    initialView: 'dayGridMonth',
    aspectRatio: 2,
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
          // 이벤트 형식 변환
          const events = res.map(item => {
            // 출근 시간과 퇴근 시간을 가져오기
            const checkInTime = item.checkInTime ? new Date(item.checkInTime) : null;
            const checkOutTime = item.checkOutTime ? new Date(item.checkOutTime) : null;

            return {
              id: item.id,
              title: '',
              start: item.checkInTime, // 시작 시간
              end: item.checkOutTime || null, // 종료 시간이 null인 경우
              extendedProps: {
                checkInTime: checkInTime,
                checkOutTime: checkOutTime
              },
              color: item.color || '#378006' // 원하는 색상으로 설정
            };
          });

          successCallback(events); // 변환된 이벤트 배열 전달
        }
      });
    }
  });
  calendar.render();
}