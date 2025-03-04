$(document).ready(function () {
  sidebarToggle();
  profileSetting();
  menuActiveFunction();
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
