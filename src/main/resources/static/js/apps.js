let customAlert = function (title, text, icon) {
  return Swal.fire({
    title: title,
    text: text,
    icon: icon,
    width: "300px",
    confirmButtonText: "확인",
    customClass: {
      icon: "custom-icon",
      title: "custom-title",
      content: "custom-content",
      confirmButton: "alert-btn"
    }
  });
}