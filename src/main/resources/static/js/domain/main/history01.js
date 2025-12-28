/**
 * 출퇴근 기록 조회 화면 스크립트
 */

let attendanceHistoryGridApi;

/**
 * 초기화 함수
 */
$(document).ready(function () {
    initSearchForm();
    initGrid();

    // 초기 데이터 로드 (오늘 날짜, 전체 사용자)
    loadAttendanceHistory({
        workDate: DateTimeUtil.today()
    });
});

/**
 * 검색 폼 초기화
 */
let initSearchForm = () => {
    const searchFields = [
        {
            name: 'workDate',
            label: '날짜',
            type: 'date',
            colSize: 2,
            defaultValue: DateTimeUtil.today(),
            maxDate: 'today', // 미래 날짜 선택 방지 (오늘까지만)
            placeholder: '조회 날짜'
        },
        {
            name: 'userName',
            label: '사용자명',
            type: 'text',
            colSize: 2,
            placeholder: '전체'
        }
    ];

    createSearchForm('attendanceHistorySearchContainer', searchFields,
        function (searchParams) {
            // 조회 버튼 클릭 시 또는 엔터 키 입력 시
            // 유효성 검사: 날짜 또는 사용자명 중 하나는 필수
            if (!searchParams.workDate && !searchParams.userName) {
                Swal.fire({
                    icon: 'warning',
                    title: '조회 조건 필요',
                    text: '날짜 또는 사용자명 중 하나는 입력해주세요.',
                    confirmButtonText: '확인'
                });
                return;
            }
            loadAttendanceHistory(searchParams);
        },
        function () {
            // 초기화 버튼 클릭 시
            loadAttendanceHistory({
                workDate: DateTimeUtil.today()
            });
        }
    );

    // 날짜 선택 시 자동 조회 이벤트 추가
    setTimeout(() => {
        setupDateChangeEvent();
    }, 100);
};

/**
 * 날짜 선택 시 자동 조회 이벤트 설정
 */
let setupDateChangeEvent = () => {
    const workDateInput = document.getElementById('workDate');
    if (workDateInput && workDateInput._flatpickr) {
        workDateInput._flatpickr.config.onChange.push(function(selectedDates, dateStr, instance) {
            // 날짜가 선택되면 자동으로 조회
            const userName = $('#userName').val();
            const searchParams = {};

            if (dateStr && dateStr.trim() !== '') {
                searchParams.workDate = dateStr;
            }

            if (userName && userName.trim() !== '') {
                searchParams.userName = userName.trim();
            }

            // 날짜 또는 사용자명이 있으면 조회
            if (searchParams.workDate || searchParams.userName) {
                loadAttendanceHistory(searchParams);
            }
        });
    }
};

/**
 * AG-Grid 초기화
 */
let initGrid = () => {
    const columnDefs = [
        {
            headerName: '날짜',
            field: 'workDate',
            flex: 1,
            minWidth: 120,
            sortable: true,
            filter: true
        },
        {
            headerName: '사용자명',
            field: 'userName',
            flex: 1,
            minWidth: 100,
            sortable: true,
            filter: true
        },
        {
            headerName: '출근시간',
            field: 'checkInTime',
            flex: 1,
            minWidth: 120,
            sortable: true,
            valueFormatter: function (params) {
                if (!params.value) return '-';
                return params.value.substring(11, 16); // HH:mm 형식으로 표시
            }
        },
        {
            headerName: '퇴근시간',
            field: 'checkOutTime',
            flex: 1,
            minWidth: 120,
            sortable: true,
            valueFormatter: function (params) {
                if (!params.value) return '-';
                return params.value.substring(11, 16); // HH:mm 형식으로 표시
            }
        },
        {
            headerName: '근무시간',
            field: 'workHours',
            flex: 1,
            minWidth: 100,
            sortable: true,
            valueFormatter: function (params) {
                if (!params.value) return '-';
                return params.value + '시간';
            }
        },
        {
            headerName: '상태',
            field: 'leaveStatus',
            flex: 1,
            minWidth: 150,
            sortable: true,
            filter: true,
            cellRenderer: function (params) {
                // params.data를 통해 전체 row 데이터에 접근
                const checkInTime = params.data.checkInTime;
                const leaveStatus = params.value;

                // 1. 출근 시간이 없으면 미출근
                if (!checkInTime) {
                    return '<span class="badge bg-secondary">미출근</span>';
                }

                // 2. 휴가 상태가 있으면 해당 상태 표시 (연차/외근/출장)
                if (leaveStatus) {
                    let badgeClass = 'bg-info';
                    let statusText = leaveStatus;

                    switch (leaveStatus) {
                        case '연차':
                            badgeClass = 'bg-primary';
                            break;
                        case '외근':
                            badgeClass = 'bg-warning text-dark';
                            break;
                        case '출장':
                            badgeClass = 'bg-info';
                            break;
                    }

                    return `<span class="badge ${badgeClass}">${statusText}</span>`;
                }

                // 3. 출근 시간이 있고 휴가 상태가 없으면 정상 출근
                return '<span class="badge bg-success">정상 출근</span>';
            }
        }
    ];

    attendanceHistoryGridApi = initAgGrid('attendanceHistoryGrid', columnDefs, {
        pagination: true,
        paginationPageSize: 20,
        paginationPageSizeSelector: [10, 20, 50, 100],
        defaultColDef: {
            sortable: true,
            filter: true,
            resizable: true
        }
    });
};

/**
 * 출퇴근 기록 데이터 로드
 */
let loadAttendanceHistory = (searchParams) => {
    $.ajax({
        url: '/api/v1/attendance/history',
        type: 'GET',
        data: searchParams,
        success: function (response) {
            if (attendanceHistoryGridApi) {
                attendanceHistoryGridApi.setGridOption('rowData', response);
            }
        },
        error: function (xhr) {
            handleAjaxError(xhr, '출퇴근 기록 조회 중 오류가 발생했습니다.');
        }
    });
};

/**
 * DateTimeUtil - 날짜 유틸리티
 */
const DateTimeUtil = {
    /**
     * 오늘 날짜 반환 (yyyy-MM-dd)
     */
    today: function () {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }
};
