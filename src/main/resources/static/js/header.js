$(document).ready(function () {
  sidebarToggle();
  profileSetting();
  menuActiveFunction();
  onClickLogoutBtn();
});

let sidebarToggle = function () {
  $(".arrow").click(function () {
    $(this).parent().parent().toggleClass("showMenu");
  });

  $(".bx-menu").click(function () {
    $(".sidebar").toggleClass("close");
  });
}

let profileSetting = function () {
  $("#profileName").html(loginUser);
  $("#rank").html(rank);
}

let menuActiveFunction = function () {
  const currentPath = window.location.pathname;
  const menuLinks = [...document.querySelectorAll(".nav-links li a")];
  menuLinks.forEach(link => {
    if (link.getAttribute("href") === currentPath) {
      link.classList.add("active"); // 현재 페이지와 일치하면 active 클래스 추가
    }
  });
}
let onClickLogoutBtn = function () {
  $("#logout-btn").click(function (event) {
    event.preventDefault(); // 기본 이동 방지

    $.ajax({
      type: "POST",
      url: "/logout",
      success: function () {
        customAlert("로그아웃", "성공적으로 로그아웃되었습니다.", "success").then(() => {
          window.location.href = "/login"; // 로그인 페이지로 이동
        });
      },
      error: function () {
        customAlert("로그아웃", "로그아웃 중 오류가 발생했습니다.", "error");
      }
    });
  });
}