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