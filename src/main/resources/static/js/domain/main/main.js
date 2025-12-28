const apiUri = "/api/v1"
let myRequestsGridApi;

$(document).ready(function () {
  onClickCard();
  checkInTimeExists();
  checkInOutBtnControll();
  onClickCheckInBtn();
  onClickCheckOutBtn();
  initMyRequestsGrid();
});

// 출근시간 데이터가 존재하지 않으면 출근 알람 팝업을 호출
const checkInTimeExists = () => {
  if (!checkInExists && !checkOutExists) {
    checkAlert("출근", "출근 체크 하시겠습니까?", "question", "출근").then((result) => {
      if (result.isConfirmed) {
        $.post(apiUri + "/attendance/check-in", null, function (res) {
          if (res.status === 200) {
            customAlert("출근 체크", res.message, "success").then(() => {
              location.reload();
            });
          } else {
            customAlert("출근 체크", res.message, "error");
          }
        })
      }
    });
  } else {
    // 출근 시간 이후 8시간이 지난 상태면 퇴근 체크를 할지 여부 확인
    if (checkOutAvailable && !checkOutExists) {
      checkAlert("퇴근", "퇴근 체크 하시겠습니까?", "question", "퇴근").then((result) => {
        if (result.isConfirmed) {
          $.post(apiUri + "/attendance/check-out", null, function (res) {
            if (res.status === 200) {
              customAlert("퇴근 체크", res.message, "success").then(() => {
                location.reload();
              });
            } else {
              customAlert("퇴근 체크", res.message, "error");
            }
          });
        }
      });
    }
  }
};

const checkInOutBtnControll = () => {
  if (checkInExists && checkOutExists) {
    $("#checkInBtn").addClass('hidden');
    $("#checkOutBtn").addClass('hidden');
  } else if (checkInExists) {
    $("#checkInBtn").addClass('hidden');
    $("#checkOutBtn").removeClass('hidden');
  } else {
    $("#checkInBtn").removeClass('hidden');
    $("#checkOutBtn").addClass('hidden');
  }
}

const onClickCheckInBtn = () => {
  $("#checkInBtn").click(function () {
    checkAlert("출근", "출근 체크 하시겠습니까?", "question", "출근").then((result) => {
      if (result.isConfirmed) {
        $.post(apiUri + "/attendance/check-in", null, function (res) {
          if (res.status === 200) {
            customAlert("출근 체크", res.message, "success").then(() => {
              location.reload();
            });
          } else {
            customAlert("출근 체크", res.message, "error");
          }
        })
      }
    });
  });
}

const onClickCheckOutBtn = () => {
  $("#checkOutBtn").click(function () {
    checkAlert("퇴근", "퇴근 체크 하시겠습니까?", "question", "퇴근").then((result) => {
      if (result.isConfirmed) {
        $.post(apiUri + "/attendance/check-out", null, function (res) {
          if (res.status === 200) {
            customAlert("퇴근 체크", res.message, "success").then(() => {
              location.reload();
            });
          } else {
            customAlert("퇴근 체크", res.message, "error");
          }
        });
      }
    });
  });
}

let onClickCard = function () {
  $(".card").on("click", function () {
    const url = $(this).data("url");
    if (url) {
      window.location.href = url;
    }
  });
}

// 나의 신청 내역 그리드 초기화
let initMyRequestsGrid = function () {
  const columnDefs = [
    {
      headerName: '신청 유형',
      field: 'requestType',
      width: 120,
      cellRenderer: function(params) {
        const typeConfig = {
          '출퇴근변경': { color: '#6c757d', icon: 'bx-time' },
          '연차': { color: '#28a745', icon: 'bx-calendar' },
          '외근': { color: '#17a2b8', icon: 'bx-walk' },
          '출장': { color: '#007bff', icon: 'bx-briefcase' }
        };
        const config = typeConfig[params.value] || { color: '#6c757d', icon: 'bx-list-ul' };
        return `<span style="color: ${config.color};"><i class="bx ${config.icon}"></i> ${params.value}</span>`;
      }
    },
    {
      headerName: '신청 날짜',
      field: 'requestDate',
      width: 120
    },
    {
      headerName: '신청 내용',
      field: 'requestContent',
      flex: 1,
      minWidth: 200
    },
    {
      headerName: '신청 상태',
      field: 'status',
      width: 100,
      cellRenderer: statusCellRenderer
    },
    {
      headerName: '신청일시',
      field: 'createdAt',
      width: 160,
      valueFormatter: function(params) {
        if (!params.value) return '';
        return new Date(params.value).toLocaleString('ko-KR');
      }
    }
  ];

  // apps.js의 공통 함수 사용
  myRequestsGridApi = initAgGrid('myRequestsGrid', columnDefs, {
    pagination: true,
    paginationPageSize: 10,
    paginationPageSizeSelector: [10, 20, 50, 100],
    rowData: []
  });

  // 데이터 로드
  loadMyRequests();
}

// 나의 신청 내역 데이터 로드
let loadMyRequests = function () {
  const requests = [];

  // 출퇴근 변경 신청 조회
  $.get(apiUri + "/attendance/request/my", function(attendanceRequests) {
    attendanceRequests.forEach(request => {
      requests.push({
        id: request.id,
        requestType: '출퇴근변경',
        requestDate: request.workDate,
        requestContent: `출근: ${formatTime(request.requestedCheckInTime)} / 퇴근: ${formatTime(request.requestedCheckOutTime)}`,
        status: request.status,
        createdAt: request.createdAt
      });
    });

    // 연차/외근/출장 신청 조회
    $.get(apiUri + "/leave/request/my", function(leaveRequests) {
      leaveRequests.forEach(request => {
        let leaveTypeText = '';
        switch(request.leaveType) {
          case 'ANNUAL_LEAVE': leaveTypeText = '연차'; break;
          case 'FIELD_WORK': leaveTypeText = '외근'; break;
          case 'BUSINESS_TRIP': leaveTypeText = '출장'; break;
        }

        let periodText = '';
        switch(request.periodType) {
          case 'ALL_DAY': periodText = '종일'; break;
          case 'MORNING': periodText = '오전'; break;
          case 'AFTERNOON': periodText = '오후'; break;
        }

        let content = `${periodText}`;
        if (request.location) {
          content += ` / ${request.location}`;
        }

        requests.push({
          id: request.id,
          requestType: leaveTypeText,
          requestDate: request.requestDate,
          requestContent: content,
          status: request.status,
          createdAt: request.createdAt
        });
      });

      // 날짜순으로 정렬 (최신순)
      requests.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

      // 그리드에 데이터 설정
      myRequestsGridApi.setGridOption('rowData', requests);
    }).fail(function(error) {
      console.error('휴가 요청 조회 실패:', error);
    });
  }).fail(function(error) {
    console.error('근태 변경 요청 조회 실패:', error);
  });
}

// 시간 포맷 함수
let formatTime = function(dateTimeStr) {
  if (!dateTimeStr) return '-';
  const date = new Date(dateTimeStr);
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${hours}:${minutes}`;
}