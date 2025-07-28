const apiUri = "/api/v1/attendance"

$(document).ready(function() {
    initAttendanceRequestGrid();
});

let initAttendanceRequestGrid = () => {
    $.get(apiUri + "/request/all", searchParams, function (res) {
        if (res.error) {
            customAlert("조회 실패", res.message, "error");
        } else {

        }
    });
    const gridOptions = {
        rowData: [
            { status: "REQUEST", user: "admin01", createdAt: "2025-07-09:T000000", requestedCheckInTime: "09:00", requestedCheckOutTime: "18:00"},
            { status: "REQUEST", user: "admin01", createdAt: "2025-07-09:T000000", requestedCheckInTime: "09:00", requestedCheckOutTime: "18:00"},
            { status: "APPROVAL", user: "admin01", createdAt: "2025-07-09:T000000", requestedCheckInTime: "09:00", requestedCheckOutTime: "18:00"},
            { status: "REJECT", user: "admin01", createdAt: "2025-07-09:T000000", requestedCheckInTime: "09:00", requestedCheckOutTime: "18:00"}
        ],
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
            { field: "createdAt", headerName: "신청일", valueFormatter: p => `${p.value.substring(0,10)}`},
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
    agGrid.createGrid(myGridElement, gridOptions);
};