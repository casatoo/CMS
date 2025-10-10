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

        // apps.js의 공통 검증 함수 사용
        if (!validateRequiredFields({
            checkInTime: { value: checkInTime, message: "출근시간을 입력해주세요" },
            checkOutTime: { value: checkOutTime, message: "퇴근시간을 입력해주세요" },
            reason: { value: reason, message: "신청사유를 입력해주세요" }
        })) {
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
                    attendance : {
                        workDate : selectedDateStr
                    },
                    status : "REQUEST",
                    requestedCheckInTime: requestedCheckInTime,
                    requestedCheckOutTime: requestedCheckOutTime,
                    reason: reason
                };

                // apps.js의 공통 POST 함수 사용
                postRequest(
                    apiUri + "/change/request",
                    params,
                    function(res) {
                        if (res.status === 200) {
                            customAlert("변경신청", res.message, "success").then(() => {
                                $("#dateModal").modal('hide');
                            });
                        } else {
                            customAlert("변경신청", res.message, "error");
                            $("#dateModal").modal('hide');
                        }
                    },
                    function(errorMessage) {
                        customAlert("변경신청 오류", errorMessage, "error");
                    }
                );
            }
        });
    });
}
