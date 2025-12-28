/**
 * 관리자 대시보드 JavaScript
 *
 * ApexCharts를 사용한 통계 시각화
 */

const apiUri = "/api/v1";
let attendanceChart;
let pendingRequestsChart;

$(document).ready(function () {
    // 페이지 로드 시 대시보드 데이터 조회
    loadDashboardStats();

    // 5분마다 자동 갱신
    setInterval(loadDashboardStats, 5 * 60 * 1000);
});

/**
 * 대시보드 통계 데이터 로드
 */
let loadDashboardStats = function () {
    $.get(apiUri + "/dashboard/stats", function(response) {
        if (response.status === 200 && response.payload) {
            const stats = response.payload;

            // 상단 카드 업데이트
            updateStatCards(stats);

            // 차트 업데이트
            updateAttendanceChart(stats);
            updatePendingRequestsChart(stats);

            // 최종 업데이트 시간
            $('#lastUpdated').text(formatDateTime(stats.calculatedAt));
        }
    }).fail(function(xhr) {
        const errorMessage = handleAjaxError(xhr);
        customAlert("오류", errorMessage, "error");
    });
};

/**
 * 통계 카드 업데이트
 */
let updateStatCards = function(stats) {
    $('#totalUsers').text(stats.totalUsers);
    $('#todayCheckIn').text(stats.todayCheckInCount);
    $('#notCheckedIn').text(stats.notCheckedInCount);
    $('#totalPending').text(stats.totalPendingRequests);
};

/**
 * 출근 현황 도넛 차트 업데이트
 */
let updateAttendanceChart = function(stats) {
    const options = {
        series: [stats.todayCheckInCount, stats.notCheckedInCount],
        chart: {
            type: 'donut',
            height: 300
        },
        labels: ['출근', '미출근'],
        colors: ['#28a745', '#ffc107'],
        legend: {
            position: 'bottom'
        },
        dataLabels: {
            enabled: true,
            formatter: function (val, opts) {
                return opts.w.config.series[opts.seriesIndex] + ' 명';
            }
        },
        plotOptions: {
            pie: {
                donut: {
                    labels: {
                        show: true,
                        total: {
                            show: true,
                            label: '출근율',
                            formatter: function () {
                                return stats.attendanceRate + '%';
                            }
                        }
                    }
                }
            }
        },
        tooltip: {
            y: {
                formatter: function (val) {
                    return val + ' 명';
                }
            }
        }
    };

    if (attendanceChart) {
        attendanceChart.destroy();
    }
    attendanceChart = new ApexCharts(
        document.querySelector("#attendanceChart"),
        options
    );
    attendanceChart.render();
};

/**
 * 승인 대기 현황 막대 차트 업데이트
 */
let updatePendingRequestsChart = function(stats) {
    const options = {
        series: [{
            name: '대기 건수',
            data: [
                stats.pendingAttendanceChangeRequests,
                stats.pendingLeaveRequests
            ]
        }],
        chart: {
            type: 'bar',
            height: 300,
            toolbar: {
                show: false
            }
        },
        plotOptions: {
            bar: {
                horizontal: false,
                columnWidth: '55%',
                endingShape: 'rounded',
                dataLabels: {
                    position: 'top'
                }
            }
        },
        dataLabels: {
            enabled: true,
            offsetY: -20,
            style: {
                fontSize: '12px',
                colors: ["#304758"]
            },
            formatter: function (val) {
                return val + ' 건';
            }
        },
        xaxis: {
            categories: ['근태 수정 요청', '휴가 신청'],
            labels: {
                style: {
                    fontSize: '12px'
                }
            }
        },
        yaxis: {
            title: {
                text: '건수'
            }
        },
        colors: ['#dc3545'],
        fill: {
            opacity: 1
        },
        tooltip: {
            y: {
                formatter: function (val) {
                    return val + ' 건';
                }
            }
        }
    };

    if (pendingRequestsChart) {
        pendingRequestsChart.destroy();
    }
    pendingRequestsChart = new ApexCharts(
        document.querySelector("#pendingRequestsChart"),
        options
    );
    pendingRequestsChart.render();
};

/**
 * 날짜/시간 포맷 함수
 */
let formatDateTime = function(dateTimeStr) {
    if (!dateTimeStr) return '-';
    const date = new Date(dateTimeStr);
    return date.toLocaleString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
};
