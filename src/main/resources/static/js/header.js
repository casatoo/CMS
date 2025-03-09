$(document).ready(function () {
  sidebarToggle();
  profileSetting();
  menuActiveFunction();
  onClickNavLink();
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
  menuLinks.forEach(link => link.classList.remove("active"));
  menuLinks.forEach(link => {
    if (link.getAttribute("href") === currentPath) {
      link.classList.add("active");
    }
  });
};

let onClickNavLink = function () {
  $(".nav-links a").click(function (event) {
    event.preventDefault(); // 기본 이동 막기

    let url = $(this).attr("href"); // 클릭된 링크의 URL 가져오기
    loadContent(url);
  });
}

function loadContent(url) {
  $.ajax({
    url: url,
    type: "GET",
    dataType: "html",
    success: function (response) {
      let newContent = $(response).find(".content").html();
      $("#content-area").html(newContent); // content 영역만 변경
      history.pushState(null, null, url);
      menuActiveFunction();
    },
    error: function () {
      alert("페이지 로드 중 오류가 발생했습니다.");
    }
  });
}

// 뒤로가기 시 AJAX로 다시 로드
window.onpopstate = function () {
  loadContent(location.pathname);
};

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