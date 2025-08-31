const apiUri = "/api/v1/attendance"
let attendanceGrid = null;
const searchParams = {};

$(document).ready(function() {
    initAttendanceRequestGrid();
});

let initAttendanceRequestGrid = () => {
    const gridOptions = {
        rowData: [], // 초기엔 빈 값
        columnDefs: [
            {
                field: "status",
                headerName: "신청상태",
                cellRenderer: params => {
                    let label = '';
                    let color = '';

                    switch (params.value) {
                        case 'REQUEST':
                            label = '신청';
                            color = 'blue';
                            break;
                        case 'APPROVAL':
                            label = '승인';
                            color = 'green';
                            break;
                        case 'REJECT':
                            label = '반려';
                            color = 'red';
                            break;
                        default:
                            label = params.value;
                            color = 'gray';
                    }

                    return `<span style="
                        background-color: ${color};
                        color: white;
                        padding: 2px 6px;
                        border-radius: 4px;
                        font-size: 12px;
                    ">${label}</span>`;
                }
            },
            { field: "user", headerName: "신청자" },
            { field: "createdAt", headerName: "신청일", valueFormatter: p => `${p.value?.substring(0,10) || ''}`},
            { field: "requestedCheckInTime", headerName: "변경출근시간" },
            { field: "requestedCheckOutTime", headerName: "변경퇴근시간" }
        ],
        pagination: true,
        paginationPageSize: 20,
        defaultColDef: {
            sortable: true,
            filter: true,
            resizable: true
        }
    };

    const myGridElement = document.querySelector('#myGrid');
    attendanceGrid = agGrid.createGrid(myGridElement, gridOptions);

    // API 호출 후 데이터 주입
    const searchParams = {}; // 필요시 검색 조건 추가
    $.get(apiUri + "/request/all", searchParams, function (res) {
        if (res.error) {
            customAlert("조회 실패", res.message, "error");
        } else {
            attendanceGrid.setGridOption('rowData', res.data || []); // res.data에 리스트가 있다고 가정
        }
    });
};