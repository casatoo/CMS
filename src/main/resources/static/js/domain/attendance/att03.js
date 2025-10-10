const apiUri = "/api/v1/leave"
let leaveRequestGridApi = null;

$(document).ready(function() {
    initSearchForm();
    initLeaveRequestGrid();
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
        },
        {
            name: 'leaveType',
            label: '신청유형',
            type: 'select',
            colSize: 1,
            options: [
                { value: 'ANNUAL_LEAVE', label: '연차' },
                { value: 'FIELD_WORK', label: '외근' },
                { value: 'BUSINESS_TRIP', label: '출장' }
            ]
        }
    ];

    // apps.js의 공통 함수 사용
    createSearchForm(
        'leaveRequestSearchContainer',
        searchFields,
        function(searchParams) {
            // 검색 버튼 클릭 시
            loadLeaveRequests(searchParams);
        },
        function() {
            // 초기화 버튼 클릭 시
            loadLeaveRequests({});
        }
    );
};

let initLeaveRequestGrid = () => {
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
            field: "leaveType",
            headerName: "신청유형",
            flex: 1,
            minWidth: 100,
            valueFormatter: params => {
                if (!params.value) return '-';
                switch (params.value) {
                    case 'ANNUAL_LEAVE': return '연차';
                    case 'FIELD_WORK': return '외근';
                    case 'BUSINESS_TRIP': return '출장';
                    default: return params.value;
                }
            }
        },
        {
            field: "requestDate",
            headerName: "신청날짜",
            flex: 1.2,
            minWidth: 120
        },
        {
            field: "periodType",
            headerName: "기간",
            flex: 0.8,
            minWidth: 80,
            valueFormatter: params => {
                if (!params.value) return '-';
                switch (params.value) {
                    case 'ALL_DAY': return '종일';
                    case 'MORNING': return '오전';
                    case 'AFTERNOON': return '오후';
                    default: return params.value;
                }
            }
        },
        {
            field: "location",
            headerName: "장소",
            flex: 1.5,
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
        }
    ];

    // apps.js의 공통 함수 사용
    leaveRequestGridApi = initAgGrid('leaveRequestGrid', columnDefs);

    // 초기 데이터 로드
    loadLeaveRequests({});
};

// 데이터 로드 함수
let loadLeaveRequests = (searchParams) => {
    $.get(apiUri + "/request/search", searchParams, function (res) {
        leaveRequestGridApi.setGridOption('rowData', res || []);
    }).fail(function(xhr, status, error) {
        customAlert("조회 실패", "연차/외근/출장 신청 목록을 불러오는데 실패했습니다.", "error");
    });
};
