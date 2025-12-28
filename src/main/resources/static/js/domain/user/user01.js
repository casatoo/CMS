const apiUri = "/api/v1/user";
let usersGridApi = null;

$(document).ready(function() {
    initSearchForm();
    initUsersGrid();
});

// 검색 폼 초기화
let initSearchForm = () => {
    const searchFields = [
        {
            name: 'role',
            label: '권한',
            type: 'select',
            colSize: 1,
            options: [
                { value: 'ROLE_ADMIN', label: '관리자' },
                { value: 'ROLE_USER', label: '사용자' }
            ]
        },
        {
            name: 'name',
            label: '이름',
            type: 'text',
            colSize: 2,
            placeholder: '사용자 이름'
        },
        {
            name: 'loginId',
            label: '로그인ID',
            type: 'text',
            colSize: 2,
            placeholder: '로그인 아이디'
        },
        {
            name: 'position',
            label: '직급',
            type: 'text',
            colSize: 1,
            placeholder: '직급'
        }
    ];

    // apps.js의 공통 함수 사용
    createSearchForm(
        'userSearchContainer',
        searchFields,
        function(searchParams) {
            // 검색 버튼 클릭 시
            loadUsers(searchParams);
        },
        function() {
            // 초기화 버튼 클릭 시
            loadUsers({});
        }
    );
};

// 사용자 그리드 초기화
let initUsersGrid = () => {
    const columnDefs = [
        {
            headerName: '사용자 ID',
            field: 'id',
            flex: 1.5,
            minWidth: 150,
            hide: true
        },
        {
            headerName: '로그인 ID',
            field: 'loginId',
            flex: 1,
            minWidth: 120,
            cellStyle: { fontWeight: '500' }
        },
        {
            headerName: '이름',
            field: 'name',
            flex: 0.8,
            minWidth: 100,
            cellRenderer: function(params) {
                return `<span style="font-weight: 500;">${params.value}</span>`;
            }
        },
        {
            headerName: '직급',
            field: 'position',
            flex: 0.8,
            minWidth: 100
        },
        {
            headerName: '권한',
            field: 'role',
            flex: 0.8,
            minWidth: 100,
            cellRenderer: function(params) {
                const roleConfig = {
                    'ROLE_ADMIN': { label: '관리자', color: '#dc3545' },
                    'ROLE_USER': { label: '사용자', color: '#0d6efd' }
                };
                const config = roleConfig[params.value] || { label: params.value, color: '#6c757d' };
                return `<span style="background-color: ${config.color}; color: white; padding: 4px 12px; border-radius: 4px; font-size: 12px; font-weight: 500;">${config.label}</span>`;
            }
        },
        {
            headerName: '남은 연차',
            field: 'annualLeaveDays',
            flex: 0.8,
            minWidth: 100,
            cellRenderer: function(params) {
                const days = params.value || 0;
                let color = '#28a745'; // 녹색
                if (days <= 5) {
                    color = '#dc3545'; // 빨간색
                } else if (days <= 10) {
                    color = '#ffc107'; // 노란색
                }
                return `<span style="color: ${color}; font-weight: 500;">${days}일</span>`;
            }
        },
        {
            headerName: '생성일시',
            field: 'createdAt',
            flex: 1.2,
            minWidth: 150,
            valueFormatter: function(params) {
                if (!params.value) return '';
                return params.value.replace('T', ' ').substring(0, 19);
            }
        },
        {
            headerName: '수정일시',
            field: 'updatedAt',
            flex: 1.2,
            minWidth: 150,
            valueFormatter: function(params) {
                if (!params.value) return '';
                return params.value.replace('T', ' ').substring(0, 19);
            }
        }
    ];

    // apps.js의 공통 함수 사용
    usersGridApi = initAgGrid('usersGrid', columnDefs, {
        pagination: true,
        paginationPageSize: 20,
        paginationPageSizeSelector: [10, 20, 50, 100],
        onRowClicked: function(event) {
            // row 클릭 시 상세 모달 열기
            if (event.data && typeof openUserDetailModal === 'function') {
                openUserDetailModal(event.data);
            }
        }
    });

    // 초기 데이터 로드
    loadUsers({});
};

// 사용자 데이터 로드
let loadUsers = (searchParams) => {
    const params = searchParams || {};
    $.get(apiUri + "/all", params, function(users) {
        usersGridApi.setGridOption('rowData', users || []);
    }).fail(function(xhr, status, error) {
        console.error('사용자 목록 조회 실패:', error);
        const errorMessage = handleAjaxError(xhr, "사용자 목록을 불러오는데 실패했습니다.");
        customAlert("조회 오류", errorMessage, "error");
    });
};
