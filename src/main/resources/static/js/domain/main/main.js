const apiUri = "/api/v1"

$(document).ready(function () {
  checkInTimeExists();
});

// 출근시간 데이터가 존재하지 않으면 출근 알람 팝업을 호출
const checkInTimeExists = () => { // 화살표 함수 한번 써본다
  if (!checkInExists) {
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
    if (checkOutAvailable) {
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
