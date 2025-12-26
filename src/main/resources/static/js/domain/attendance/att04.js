const apiUri = "/api/v1/attendance"
let attendanceRequestGridApi = null;

$(document).ready(function() {
    initSearchForm();
    initAttendanceRequestGrid();
});

// 검색 폼 초기화
let initSearchForm = () => {
    const searchFields = [
        {
            name: 'status',
            label: '신청상태',
            type: 'select',
            colSize: 1,
            options: [
                { value: 'REQUEST', label: '신청' },
                { value: 'APPROVE', label: '승인' },
                { value: 'REJECT', label: '반려' }
            ]
        },
        {
            name: 'userName',
            label: '신청자',
            type: 'text',
            colSize: 2,
            placeholder: '신청자명'
        }
    ];

    // apps.js의 공통 함수 사용
    createSearchForm(
        'attendanceRequestSearchContainer',
        searchFields,
        function(searchParams) {
            // 검색 버튼 클릭 시
            loadAttendanceRequests(searchParams);
        },
        function() {
            // 초기화 버튼 클릭 시
            loadAttendanceRequests({});
        }
    );
};

let initAttendanceRequestGrid = () => {
    const columnDefs = [
        {
            field: "status",
            headerName: "신청상태",
            flex: 1,
            minWidth: 100,
            cellRenderer: statusCellRenderer // apps.js의 공통 함수 사용
        },
        {
            field: "userName",
            headerName: "신청자",
            flex: 1,
            minWidth: 100,
            valueGetter: params => params.data?.userName || params.data?.userLoginId || '-'
        },
        {
            field: "workDate",
            headerName: "근무일",
            flex: 1.2,
            minWidth: 120,
            valueFormatter: params => params.value || '-'
        },
        {
            field: "createdAt",
            headerName: "신청일시",
            flex: 1.5,
            minWidth: 150,
            valueFormatter: params => {
                if (!params.value) return '-';
                return params.value.replace('T', ' ').substring(0, 19);
            }
        },
        {
            field: "requestedCheckInTime",
            headerName: "변경 출근시간",
            flex: 1.5,
            minWidth: 150,
            valueFormatter: params => {
                if (!params.value) return '-';
                return params.value.replace('T', ' ').substring(0, 19);
            }
        },
        {
            field: "requestedCheckOutTime",
            headerName: "변경 퇴근시간",
            flex: 1.5,
            minWidth: 150,
            valueFormatter: params => {
                if (!params.value) return '-';
                return params.value.replace('T', ' ').substring(0, 19);
            }
        }
    ];

    // apps.js의 공통 함수 사용
    attendanceRequestGridApi = initAgGrid('attendanceRequestGrid', columnDefs, {
        onRowClicked: function(event) {
            // row 클릭 시 상세 모달 열기
            if (event.data && typeof openAttendanceRequestDetailModal === 'function') {
                openAttendanceRequestDetailModal(event.data);
            }
        }
    });

    // 초기 데이터 로드
    loadAttendanceRequests({});
};

// 데이터 로드 함수
let loadAttendanceRequests = (searchParams) => {
    $.get(apiUri + "/request/search", searchParams, function (res) {
        attendanceRequestGridApi.setGridOption('rowData', res || []);
    }).fail(function(xhr, status, error) {
        customAlert("조회 실패", "근태 신청 목록을 불러오는데 실패했습니다.", "error");
    });
};
