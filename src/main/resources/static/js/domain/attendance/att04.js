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
                width: 120,
                cellRenderer: params => {
                    let label = '';
                    let color = '';

                    switch (params.value) {
                        case 'REQUEST':
                            label = '신청';
                            color = '#0d6efd';
                            break;
                        case 'APPROVE':
                            label = '승인';
                            color = '#198754';
                            break;
                        case 'REJECT':
                            label = '반려';
                            color = '#dc3545';
                            break;
                        default:
                            label = params.value;
                            color = '#6c757d';
                    }

                    return `<span style="
                        background-color: ${color};
                        color: white;
                        padding: 4px 8px;
                        border-radius: 4px;
                        font-size: 12px;
                        font-weight: 500;
                    ">${label}</span>`;
                }
            },
            {
                field: "userName",
                headerName: "신청자",
                width: 120,
                valueGetter: params => params.data?.userName || params.data?.userLoginId || '-'
            },
            {
                field: "createdAt",
                headerName: "신청일시",
                width: 180,
                valueFormatter: params => {
                    if (!params.value) return '-';
                    return params.value.replace('T', ' ').substring(0, 19);
                }
            },
            {
                field: "requestedCheckInTime",
                headerName: "변경 출근시간",
                width: 180,
                valueFormatter: params => {
                    if (!params.value) return '-';
                    return params.value.replace('T', ' ').substring(0, 19);
                }
            },
            {
                field: "requestedCheckOutTime",
                headerName: "변경 퇴근시간",
                width: 180,
                valueFormatter: params => {
                    if (!params.value) return '-';
                    return params.value.replace('T', ' ').substring(0, 19);
                }
            }
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
    $.get(apiUri + "/request/all", function (res) {
        console.log('API Response:', res);
        attendanceGrid.setGridOption('rowData', res || []);
    }).fail(function(xhr, status, error) {
        console.error('API Error:', error);
        customAlert("조회 실패", "근태 신청 목록을 불러오는데 실패했습니다.", "error");
    });
};