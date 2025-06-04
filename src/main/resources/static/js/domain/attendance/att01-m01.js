let initDateModal = function () {
    $('#dateModal').on('shown.bs.modal', function () {

    });
    $('#dateModal').on('hide.bs.modal', function () {
        $(document.activeElement).blur();
        $('#startTime').val('');
        $('#endTime').val('');
        $('#reason').val('');
    });
}

let onClickAttendanceChangeRequestBtn = function () {
    $("#attendanceChangeRequestBtn").click(function () {

        let checkInTime = $("#startTime").val();
        let checkOutTime = $("#endTime").val();
        let reason = $("#reason").val();

        if (_.isEmpty(checkInTime)) {
            customAlert("필수항목", "출근시간을 입력해주세요", "warning");
            return;
        }

        if (_.isEmpty(checkOutTime)) {
            customAlert("필수항목", "퇴근시간을 입력해주세요", "warning");
            return;
        }

        if (_.isEmpty(reason.trim())) {
            customAlert("필수항목", "신청사유를 입력해주세요", "warning");
            return;
        }

        checkAlert("변경신청", "변경신청 하시겠습니까?", "question", "신청").then((result) => {
            if (result.isConfirmed) {

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
                    reason: reason
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
