let customAlert = function(title, text, icon) {
  return Swal.fire({
    title: title,
    text: text,
    icon: icon,
    width: "400px",
    confirmButtonText: "확인",
    customClass: {
      icon: "custom-icon",
      title: "custom-title",
      content: "custom-content",
      confirmButton: "alert-btn"
    }
  });
}

let checkAlert = function(title, text, icon, confirmButtonText) {
  return Swal.fire({
    title: title,
    text: text,
    icon: icon,
    width: "400px",
    showCancelButton: true,
    confirmButtonText: confirmButtonText,
    cancelButtonText: '취소',
    customClass: {
      icon: "custom-icon",
      title: "custom-title",
      content: "custom-content",
      confirmButton: "alert-btn"
    }
  });
}

let initializeDataTable = function(tableId, columns, columnDefs, ajaxUrl, gridHeight) {
  return $(tableId).DataTable({
    autoWidth: false,
    dom: '<"top d-flex justify-content-end"f>rt<"bottom d-flex justify-content-between"p l><"clear">',
    language: {
      lengthMenu: "Page: _MENU_",
      search: "검색: ",
      paginate: {
        first: "<<",
        last: ">>",
        next: ">",
        previous: "<"
      }
    },
    scrollY: gridHeight,
    scrollCollapse: true,
    ajax: {
      url: ajaxUrl,
      dataSrc: function (json) {
        return json
      }
    },
    columns: columns,
    columnDefs: columnDefs,
    initComplete: function () {
      $(tableId + ' thead th').css('text-align', 'center');
      $('.dt-search label').css('margin-right', '5px');
      $('.dt-length label').css('margin-right', '5px');
      $('.dt-search .dt-input').css('width', '300px');
      $('.dt-container .top').css('margin-top', '10px').css('margin-bottom', '10px');
      $('.dt-container .bottom').css('margin-top', '10px').css('margin-bottom', '10px');
      $(tableId + ' th, ' + tableId + ' td').css({
        'border': '1px solid #ccc',
        'padding': '10px'
      });
    }
  });
}

let initTimePicker = function(id, defaultTime) {
  let $input = $('#' + id);
  if (!$input.hasClass('timepicker-initialized')) {
    $input.timepicker({
      timeFormat: 'HH:mm',
      interval: 30,
      defaultTime: defaultTime,
      dynamic: false,
      dropdown: true,
      scrollbar: true
    }).addClass('timepicker-initialized');
  }

  $input.timepicker('setTime', defaultTime);
}

$.fn.serializeObject = function () {
  "use strict";
  const result = {};
  const now = new Date();

  const pad = (n) => (n < 10 ? "0" + n : n);

  const toLocalDateTime = (timeStr) => {
    if (!timeStr || !/^\d{2}:\d{2}$/.test(timeStr)) return null;
    const [hour, minute] = timeStr.split(":").map(Number);
    return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}T${pad(hour)}:${pad(minute)}:00`;
  };

  this.find(':input[name]').each(function () {
    const name = this.name;
    let value = this.value.trim();
    if (!value) return;

    const typeHint = $(this).data("type");

    if (typeHint === "datetime") {
      value = toLocalDateTime(value);
    }

    const existing = result[name];
    if (typeof existing !== "undefined") {
      if (Array.isArray(existing)) {
        existing.push(value);
      } else {
        result[name] = [existing, value];
      }
    } else {
      result[name] = value;
    }
  });

  return result;
};

// ========================================
// AG-Grid 공통 함수
// ========================================

/**
 * AG-Grid를 초기화하는 공통 함수
 * @param {string} gridId - 그리드 엘리먼트 ID
 * @param {Array} columnDefs - 컬럼 정의
 * @param {Object} options - 추가 옵션 (pagination, pageSize 등)
 * @returns {Object} AG-Grid API 객체
 */
let initAgGrid = function(gridId, columnDefs, options = {}) {
  const defaultOptions = {
    rowData: [],
    columnDefs: columnDefs,
    pagination: true,
    paginationPageSize: 20,
    defaultColDef: {
      sortable: true,
      filter: true,
      resizable: true
    },
    localeText: {
      noRowsToShow: '검색 결과가 없습니다',
      loadingOoo: '로딩 중...',
      page: '페이지',
      more: '더보기',
      to: '~',
      of: '/',
      next: '다음',
      last: '마지막',
      first: '처음',
      previous: '이전',
      // 필터 관련
      filterOoo: '필터...',
      applyFilter: '필터 적용',
      clearFilter: '필터 초기화',
      equals: '같음',
      notEqual: '같지 않음',
      lessThan: '미만',
      greaterThan: '초과',
      lessThanOrEqual: '이하',
      greaterThanOrEqual: '이상',
      inRange: '범위',
      contains: '포함',
      notContains: '포함하지 않음',
      startsWith: '시작',
      endsWith: '끝',
      // 그룹 관련
      group: '그룹',
      rowGroupColumnsEmptyMessage: '여기에 열을 드래그하여 그룹화',
      // 페이지네이션
      page: '페이지',
      to: '-',
      of: '/'
    }
  };

  const gridOptions = { ...defaultOptions, ...options };
  const gridElement = document.querySelector(`#${gridId}`);
  return agGrid.createGrid(gridElement, gridOptions);
};

/**
 * AG-Grid에 검색 기능을 추가하는 공통 함수
 * @param {string} searchBarId - 검색 바 엘리먼트 ID
 * @param {Object} gridApi - AG-Grid API 객체
 */
let initAgGridSearchBar = function(searchBarId, gridApi) {
  const searchBar = document.querySelector(`#${searchBarId}`);
  if (!searchBar) {
    return;
  }

  searchBar.addEventListener('input', function(e) {
    const searchText = e.target.value;
    gridApi.setGridOption('quickFilterText', searchText);
  });
};

/**
 * AG-Grid 상태 컬럼 렌더러
 * @param {Object} params - AG-Grid 셀 렌더러 파라미터
 * @returns {string} HTML 문자열
 */
let statusCellRenderer = function(params) {
  const statusConfig = {
    'REQUEST': { label: '신청', color: '#0d6efd' },
    'APPROVE': { label: '승인', color: '#198754' },
    'REJECT': { label: '반려', color: '#dc3545' }
  };

  const config = statusConfig[params.value] || { label: params.value, color: '#6c757d' };

  return `<span style="
    background-color: ${config.color};
    color: white;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 500;
  ">${config.label}</span>`;
};

// ========================================
// FullCalendar 공통 함수
// ========================================

/**
 * FullCalendar를 초기화하는 공통 함수
 * @param {string} calendarId - 캘린더 엘리먼트 ID
 * @param {Object} options - 추가 옵션 (events, dateClick 등)
 * @returns {Object} FullCalendar 인스턴스
 */
let initFullCalendar = function(calendarId, options = {}) {
  const calendarEl = $(`#${calendarId}`)[0];

  const defaultOptions = {
    locale: 'ko',
    initialView: 'dayGridMonth',
    aspectRatio: 1.8,
    headerToolbar: {
      left: 'today',
      center: 'title',
      right: 'prev,next'
    },
    buttonText: {
      today: '이번달'
    },
    eventDidMount: function(info) {
      $(info.el).closest('.fc-daygrid-day').css({
        height: '120px'
      });
      $(info.el).find('.fc-event-title').css('color', 'black');
      $(info.el).find('.fc-event-time').css('color', 'black');
    },
    datesSet: function() {
      $('.fc-col-header-cell a').css({
        'color': 'black',
        'text-decoration': 'none'
      });
      $('.fc-daygrid-day-number').css({
        'color': 'black',
        'text-decoration': 'none'
      });
    }
  };

  const calendarOptions = { ...defaultOptions, ...options };
  const calendar = new FullCalendar.Calendar(calendarEl, calendarOptions);
  calendar.render();
  return calendar;
};

/**
 * 날짜 비교 유틸리티 함수
 * @param {string} dateStr - 날짜 문자열 (YYYY-MM-DD)
 * @param {Date} compareDate - 비교할 날짜 객체 (기본값: 오늘)
 * @returns {number} -1: 이전, 0: 같음, 1: 이후
 */
let compareDateWithToday = function(dateStr, compareDate = new Date()) {
  const targetDate = new Date(dateStr);
  targetDate.setHours(0, 0, 0, 0);
  compareDate.setHours(0, 0, 0, 0);

  if (targetDate < compareDate) return -1;
  if (targetDate > compareDate) return 1;
  return 0;
};

// ========================================
// AJAX 공통 함수
// ========================================

/**
 * AJAX 에러를 처리하는 공통 함수
 * @param {Object} xhr - XMLHttpRequest 객체
 * @param {string} defaultMessage - 기본 에러 메시지
 * @returns {string} 에러 메시지
 */
let handleAjaxError = function(xhr, defaultMessage = "요청 중 오류가 발생했습니다.") {
  let errorMessage = defaultMessage;

  if (xhr.status === 400 && xhr.responseJSON) {
    const response = xhr.responseJSON;

    if (response.message) {
      errorMessage = response.message;
    }

    if (response.payload) {
      const errors = response.payload;
      const errorDetails = [];

      if (typeof errors === 'object' && !Array.isArray(errors)) {
        for (let field in errors) {
          errorDetails.push(errors[field]);
        }
        if (errorDetails.length > 0) {
          errorMessage = errorDetails.join('\n');
        }
      }
    }
  } else if (xhr.status === 401) {
    errorMessage = "인증이 필요합니다. 다시 로그인해주세요.";
  } else if (xhr.status === 403) {
    errorMessage = "권한이 없습니다.";
  } else if (xhr.status === 500) {
    errorMessage = "서버 내부 오류가 발생했습니다.";
  }

  return errorMessage;
};

/**
 * POST 요청을 보내는 공통 함수
 * @param {string} url - API URL
 * @param {Object} data - 전송할 데이터
 * @param {Function} successCallback - 성공 콜백
 * @param {Function} errorCallback - 에러 콜백 (선택)
 */
let postRequest = function(url, data, successCallback, errorCallback) {
  $.ajaxSetup({ contentType: "application/json" });
  $.post(url, JSON.stringify(data), successCallback)
    .fail(function(xhr, status, error) {
      const errorMessage = handleAjaxError(xhr);

      if (errorCallback) {
        errorCallback(errorMessage, xhr);
      } else {
        customAlert("오류", errorMessage, "error");
      }
    });
};

// ========================================
// 입력 검증 공통 함수
// ========================================

/**
 * 필수 입력값을 검증하는 공통 함수
 * @param {Object} validations - {fieldName: {value, message}}
 * @returns {boolean} 검증 통과 여부
 */
let validateRequiredFields = function(validations) {
  for (let field in validations) {
    const { value, message } = validations[field];
    if (_.isEmpty(value) || (typeof value === 'string' && _.isEmpty(value.trim()))) {
      customAlert("필수항목", message, "warning");
      return false;
    }
  }
  return true;
};

// ========================================
// 검색 폼 공통 함수
// ========================================

/**
 * DatePicker를 초기화하는 공통 함수
 * @param {string} elementId - input 엘리먼트 ID
 * @param {Object} options - flatpickr 옵션
 */
let initDatePicker = function(elementId, options = {}) {
  const defaultOptions = {
    dateFormat: "Y-m-d",
    locale: "ko",
    allowInput: true
  };

  const pickerOptions = { ...defaultOptions, ...options };
  return flatpickr(`#${elementId}`, pickerOptions);
};

/**
 * 날짜 범위 DatePicker를 초기화하는 공통 함수
 * @param {string} startElementId - 시작일 input 엘리먼트 ID
 * @param {string} endElementId - 종료일 input 엘리먼트 ID
 */
let initDateRangePicker = function(startElementId, endElementId) {
  const startPicker = initDatePicker(startElementId);
  const endPicker = initDatePicker(endElementId, {
    onChange: function(selectedDates, dateStr, instance) {
      startPicker.set('maxDate', dateStr);
    }
  });

  $(`#${startElementId}`).on('change', function() {
    endPicker.set('minDate', $(this).val());
  });

  return { startPicker, endPicker };
};

/**
 * 검색 폼의 값을 객체로 직렬화하는 함수
 * @param {string} formId - 폼 엘리먼트 ID
 * @returns {Object} 검색 파라미터 객체
 */
let serializeSearchForm = function(formId) {
  const formData = {};
  $(`#${formId}`).find('input, select').each(function() {
    const name = $(this).attr('name');
    const value = $(this).val();

    if (name && value && value.trim() !== '') {
      formData[name] = value.trim();
    }
  });
  return formData;
};

/**
 * 검색 폼을 초기화(리셋)하는 함수
 * @param {string} formId - 폼 엘리먼트 ID
 * @param {Function} callback - 초기화 후 실행할 콜백 함수
 */
let resetSearchForm = function(formId, callback) {
  $(`#${formId}`)[0].reset();

  // DatePicker 초기화
  $(`#${formId}`).find('input[type="text"]').each(function() {
    const fp = $(this)[0]._flatpickr;
    if (fp) {
      fp.clear();
    }
  });

  if (callback) {
    callback();
  }
};

/**
 * 검색 폼을 생성하는 공통 함수
 * @param {string} containerId - 검색 폼을 넣을 컨테이너 ID
 * @param {Array} searchFields - 검색 필드 정의 배열
 * @param {Function} onSearch - 검색 버튼 클릭 시 실행할 콜백
 * @param {Function} onReset - 초기화 버튼 클릭 시 실행할 콜백
 */
let createSearchForm = function(containerId, searchFields, onSearch, onReset) {
  const formId = `${containerId}Form`;
  let formHtml = `<form id="${formId}" class="search-form mb-1">
    <div class="search-form-container">
      <div class="search-fields">`;

  // 검색 필드들을 먼저 추가
  searchFields.forEach(field => {
    formHtml += `
        <div class="search-field-group">
          <label class="search-label">${field.label}</label>`;

    if (field.type === 'select') {
      formHtml += `
          <select class="search-input search-select" name="${field.name}">
            <option value="">전체</option>`;
      field.options.forEach(opt => {
        formHtml += `<option value="${opt.value}">${opt.label}</option>`;
      });
      formHtml += `</select>`;
    } else if (field.type === 'date') {
      formHtml += `
          <input type="text" class="search-input" name="${field.name}" id="${field.name}"
                 placeholder="${field.placeholder || 'YYYY-MM-DD'}">`;
    } else {
      formHtml += `
          <input type="text" class="search-input" name="${field.name}"
                 placeholder="${field.placeholder || ''}">`;
    }

    formHtml += `
        </div>`;
  });

  // 버튼 영역
  formHtml += `
      </div>
      <div class="search-buttons">
        <button type="button" class="search-btn search-btn-primary" id="${containerId}SearchBtn">
          <i class="bi bi-search"></i> 조회
        </button>
        <button type="button" class="search-btn search-btn-secondary" id="${containerId}ResetBtn">
          <i class="bi bi-arrow-clockwise"></i> 초기화
        </button>
      </div>
    </div>
  </form>`;

  $(`#${containerId}`).html(formHtml);

  // DatePicker 초기화
  searchFields.forEach(field => {
    if (field.type === 'date') {
      initDatePicker(field.name);
    }
  });

  // 이벤트 바인딩
  $(`#${containerId}SearchBtn`).on('click', function() {
    const searchParams = serializeSearchForm(formId);
    if (onSearch) {
      onSearch(searchParams);
    }
  });

  $(`#${containerId}ResetBtn`).on('click', function() {
    resetSearchForm(formId, onReset);
  });
};