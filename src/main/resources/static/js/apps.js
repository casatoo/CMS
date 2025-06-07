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
      minTime: '06:00',
      maxTime: '22:00',
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