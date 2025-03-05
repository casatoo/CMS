$(document).ready(function () {
  loginProc();
});

let loginProc = function () {
  $("#loginProc").click(function (event) {
    event.preventDefault();

    let loginId = $("#loginId").val().trim();
    let password = $("#password").val().trim();

    // 필수 값 체크
    if (_.isEmpty(loginId)) {
      customAlert("필수항목 체크", "아이디는 필수값입니다.", "warning");
      return;
    }
    if (_.isEmpty(password)) {
      customAlert("필수항목 체크", "비밀번호는 필수값입니다.", "warning");
      return;
    }
    let loginData = {
      loginId: loginId,
      password: password
    };
    $.ajax({
      type: "POST",
      url: "/loginProc",
      contentType: "application/json",
      data: JSON.stringify(loginData),
      dataType: "json",
      success: function (res) {
        if (res.status === 200) {
          customAlert("로그인 성공", res.message, "success").then(() => {
            window.location.href = "/main"; // 로그인 성공 시 메인 페이지로 이동
          });
        } else {
          customAlert("로그인 실패", res.message, "error");
        }
      },
      error: function (res) {
        let errorMessage = "로그인 중 오류가 발생했습니다.";
        if (res.responseJSON && res.responseJSON.message) {
          errorMessage = res.responseJSON.message;
        }
        customAlert("로그인 실패", errorMessage, "error");
      }
    });
  });
}