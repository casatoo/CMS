$(document).ready(function () {
  document.body.classList.add("preload");

  sidebarToggle();
  profileSetting();
  menuActiveFunction();
  onClickLogoutBtn();

  let storedStatus = JSON.parse(localStorage.getItem("sidebarStatus"));
  if (storedStatus === null) {
    storedStatus = false;
    localStorage.setItem("sidebarStatus", JSON.stringify(storedStatus));
  }
  applySideBarStatus(storedStatus);

  setTimeout(() => {
    document.body.classList.remove("preload");
  }, 50);
});


let sidebarToggle = function () {
  $(".arrow").click(function () {
    $(this).parent().parent().toggleClass("showMenu");
  });

  $(".bx-menu").click(function () {
    let sideBarStatus = JSON.parse(localStorage.getItem("sidebarStatus"));
    let newStatus = !sideBarStatus;
    localStorage.setItem("sidebarStatus", JSON.stringify(newStatus));
    applySideBarStatus(newStatus);
  });
};

let applySideBarStatus = function (status) {
  if (status) {
    $(".sidebar").removeClass("close");
  } else {
    $(".sidebar").addClass("close");
  }
};

let profileSetting = function () {
  $("#profileName").html(loginUser);
  $("#rank").html(rank);
};

let menuActiveFunction = function () {
  const currentPath = window.location.pathname;
  const menuLinks = document.querySelectorAll(".nav-links li a");

  menuLinks.forEach(link => {
    const linkPath = link.getAttribute("href");

    if (linkPath === currentPath) {
      let parentLi = link.closest("li"); // 현재 링크의 가장 가까운 <li> 찾기
      if (parentLi) {
        parentLi.classList.add("active"); // 현재 <li>에 active 추가

        // 상위 메뉴 활성화 (부모의 부모 <li>에도 active 추가)
        let parentMenu = parentLi.closest(".sub-menu");
        if (parentMenu) {
          let topMenu = parentMenu.closest("li");
          if (topMenu) {
            topMenu.classList.add("active"); // 최상위 <li>에도 active 추가
          }
        }
      }
    }
  });
};

let onClickLogoutBtn = function () {
  $("#logout-btn").click(function (event) {
    event.preventDefault();

    $.ajax({
      type: "POST",
      url: "/logout",
      success: function () {
        customAlert("로그아웃", "성공적으로 로그아웃되었습니다.", "success").then(() => {
          window.location.href = "/login";
        });
      },
      error: function () {
        customAlert("로그아웃", "로그아웃 중 오류가 발생했습니다.", "error");
      }
    });
  });
};