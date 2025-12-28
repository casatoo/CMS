let initLeaveRequestModal = function () {
    $('#leaveModal').on('shown.bs.modal', function () {
        // 모달 열릴 때 초기화
    });

    $('#leaveModal').on('hide.bs.modal', function () {
        // 모달 내부에 포커스가 있는 요소의 포커스 제거
        const focusedElement = $(this).find(':focus');
        if (focusedElement.length > 0) {
            focusedElement.blur();
        }
        $('#leaveType').val('');
        $('#periodType').val('');
        $('#location').val('');
        $('#reason').val('');
        $('#locationGroup').hide();
    });

    // 모달이 완전히 숨겨진 후 포커스 정리
    $('#leaveModal').on('hidden.bs.modal', function () {
        $(document.activeElement).blur();
    });

    // 신청 유형에 따라 장소 입력란 표시/숨김
    $('#leaveType').change(function() {
        const leaveType = $(this).val();
        if (leaveType === 'FIELD_WORK' || leaveType === 'BUSINESS_TRIP') {
            $('#locationGroup').show();
        } else {
            $('#locationGroup').hide();
            $('#location').val('');
        }
    });
}

let onClickLeaveRequestBtn = function () {
    $("#leaveRequestBtn").click(function () {
        let leaveType = $("#leaveType").val();
        let periodType = $("#periodType").val();
        let location = $("#location").val();
        let reason = $("#reason").val();

        // apps.js의 공통 검증 함수 사용
        const validations = {
            leaveType: { value: leaveType, message: "신청 유형을 선택해주세요" },
            periodType: { value: periodType, message: "기간을 선택해주세요" },
            reason: { value: reason, message: "신청사유를 입력해주세요" }
        };

        // 외근/출장인 경우 장소도 검증
        if (leaveType === 'FIELD_WORK' || leaveType === 'BUSINESS_TRIP') {
            validations.location = { value: location, message: "장소를 입력해주세요" };
        }

        if (!validateRequiredFields(validations)) {
            return;
        }

        checkAlert("신청", "신청하시겠습니까?", "question", "신청").then((result) => {
            if (result.isConfirmed) {
                let params = {
                    leaveType: leaveType,
                    requestDate: selectedDate,
                    periodType: periodType,
                    location: location,
                    reason: reason,
                    status: "REQUEST"
                };

                // apps.js의 공통 POST 함수 사용
                postRequest(
                    apiUri + "/request",
                    params,
                    function(res) {
                        if (res.status === 200) {
                            const icon = res.payload ? "success" : "info";
                            customAlert("신청", res.message, icon).then(() => {
                                $("#leaveModal").modal('hide');
                                if (res.payload && calendar) {
                                    calendar.refetchEvents();
                                }
                            });
                        } else {
                            customAlert("신청", res.message, "error");
                            $("#leaveModal").modal('hide');
                        }
                    },
                    function(errorMessage) {
                        customAlert("신청 오류", errorMessage, "error");
                    }
                );
            }
        });
    });
}