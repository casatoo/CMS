$(document).ready(function () {
  joinProc();
});

let joinProc = function () {
  $("#joinProc").click(function (event) {
    event.preventDefault();

    let loginId = $("#loginId").val().trim();
    let password = $("#password").val().trim();
    let name = $("#name").val().trim();

    // 필수 값 체크
    if (_.isEmpty(loginId)) {
      customAlert("필수항목 체크", "아이디는 필수값입니다.", "warning");
      return;
    }
    if (_.isEmpty(password)) {
      customAlert("필수항목 체크", "비밀번호는 필수값입니다.", "warning");
      return;
    }
    if (_.isEmpty(name)) {
      customAlert("필수항목 체크", "이름은 필수값입니다.", "warning");
      return;
    }

    // 특수문자 체크
    let allowedSpecialChars = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;
    let restrictedChars = /['"\\<>;:]/;

    if (allowedSpecialChars.test(loginId)) {
      customAlert("입력 오류", "아이디에 특수문자가 포함될 수 없습니다.", "warning");
      return;
    }

    let foundRestrictedCharsInPassword = _.filter(password, char => restrictedChars.test(char));
    if (!_.isEmpty(foundRestrictedCharsInPassword)) {
      customAlert("입력 오류", `${foundRestrictedCharsInPassword.join(", ")} 는 비밀번호에 사용할 수 없습니다.`, "warning");
      return;
    }

    if (restrictedChars.test(name)) {
      customAlert("입력 오류", "이름에 특수문자가 포함될 수 없습니다.", "warning");
      return;
    }

    let joinData = {
      loginId: loginId,
      password: password,
      name: name
    };

    // apps.js의 공통 함수 사용
    postRequest("/joinProc", joinData, function (res) {
      // 요청이 성공했을 때의 처리
      if (res.status === 200) {
        customAlert("회원가입", res.message, "success").then(() => {
          window.location.href = "/login"; // 회원가입 성공 시 로그인 페이지로 이동
        });
      } else {
        customAlert("회원가입", res.message, "error");
      }
    });
  });
}