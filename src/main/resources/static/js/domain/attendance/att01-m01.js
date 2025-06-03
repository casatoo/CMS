let initDateModal = function () {
    $('#dateModal').on('shown.bs.modal', function () {
        $('#startTime').focus();
    });
    $('#dateModal').on('hide.bs.modal', function () {
        document.activeElement.blur(); // aria-hidden 포커싱 문제
        $('#startTime').val('');
        $('#endTime').val('');
    });
}

let onClickAttendanceChangeRequestBtn = function () {
    $("#attendanceChangeRequestBtn").click(function () {
        checkAlert("변경신청", "변경신청 하시겠습니까?", "question", "신청").then((result) => {
            if (result.isConfirmed) {
                let checkInTime = $("#startTime").val();
                let checkOutTime = $("#endTime").val();
                let selectedDateStr = selectedDate;

                let requestedCheckInTime = selectedDateStr + "T" + checkInTime + ":00";
                let requestedCheckOutTime = selectedDateStr + "T" + checkOutTime + ":00";

                let checkInDate = new Date(requestedCheckInTime);
                let checkOutDate = new Date(requestedCheckOutTime);

                if (checkOutDate <= checkInDate) {
                    customAlert("시간 오류", "퇴근시간은 출근시간보다 늦어야 합니다.", "warning");
                    return;
                }

                let params = {
                    requestedCheckInTime: requestedCheckInTime,
                    requestedCheckOutTime: requestedCheckOutTime,
                    reason: $("#reason").val()
                };
                $.ajaxSetup({contentType:"application/json"})
                $.post(apiUri + "/change/request", JSON.stringify(params), function (res) {
                    if (res.status === 200) {
                        if(res.payload) {
                            customAlert("변경신청", res.message, "success").then(() => {
                                $("#dateModal").modal('hide');
                            });
                        } else {
                            customAlert("변경신청", res.message, "success").then(() => {
                                $("#dateModal").modal('hide');
                            });
                        }
                    } else {
                        customAlert("변경신청", res.message, "error");
                        $("#dateModal").modal('hide');
                    }
                });
            }
        });
    });
}
