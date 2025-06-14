const apiUri = "/api/v1"

$(document).ready(function () {
  onClickCard();
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

let onClickCard = function () {
  $(".card").on("click", function () {
    const url = $(this).data("url");
    if (url) {
      window.location.href = url;
    }
  });
}

let initTable = () => {

  const gridOptions = {

    rowData: [
      { make: "Tesla", model: "Model Y", price: 64950, electric: true },
      { make: "Ford", model: "F-Series", price: 33850, electric: false },
      { make: "Toyota", model: "Corolla", price: 29600, electric: false },
    ],

    columnDefs: [
      { field: "make", headerName: "제조사" },
      { field: "model", headerName: "모델" },
      { field: "price", headerName: "가격", valueFormatter: p => `${p.value.toLocaleString()} 원` },
      { field: "electric", headerName: "전기차", cellRenderer: p => p.value ? "✅" : "❌" }
    ],

    pagination: true,
    defaultColDef: {
      sortable: true,
      filter: true,
      resizable: true
    }
  };

  const myGridElement = document.querySelector('#example');
  agGrid.createGrid(myGridElement, gridOptions);
}