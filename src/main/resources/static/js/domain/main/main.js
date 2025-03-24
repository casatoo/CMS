const apiUri = "/api/v1"

$(document).ready(function () {
  checkInTimeExists();
  checkInOutBtnControll();
  onClickCheckInBtn();
  onClickCheckOutBtn();
  initTable();
});

// 출근시간 데이터가 존재하지 않으면 출근 알람 팝업을 호출
const checkInTimeExists = () => { // 화살표 함수 한번 써본다
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

document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".card").forEach(card => {
    card.addEventListener("click", function () {
      const url = this.getAttribute("data-url");
      if (url) {
        window.location.href = url;
      }
    });
  });
});

let initTable = () => {

  let columns = [
    { title: "ID", data: "id"},
    { title: "이름", data: "name"},
    { title: "직업", data: "company.name"}
  ];

  let columnDefs = [
    { targets: 0, className: 'text-center', width: '50px' },
    { targets: 1, className: 'text-left', width: '150px' },
    { targets: 2, className: 'text-left', width: '200px' }
  ];

  initializeDataTable('#example', columns, columnDefs, 'https://jsonplaceholder.typicode.com/users', '380px');
}