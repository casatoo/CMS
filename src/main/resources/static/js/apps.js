let customAlert = function (title, text, icon) {
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

let checkAlert = function (title, text, icon, confirmButtonText) {
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