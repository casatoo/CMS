$(document).ready(function () {
  document.body.classList.add("preload");

  sidebarToggle();
  profileSetting();
  menuActiveFunction();
  onClickLogoutBtn();
  contentAreaClickHandler();
  onClickProfileContent();

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
  $("#position").html(position);

  // 사용자 프로필 이미지 로드
  $.ajax({
    type: "GET",
    url: "/api/v1/profile/me",
    success: function (data) {
      if (data && data.profileImageName) {
        // 프로필 이미지가 있는 경우 실제 이미지로 변경
        const imageUrl = "/api/v1/profile/image/" + data.profileImageName;
        $("#profileImage").attr("src", imageUrl);
      }
      // 프로필 이미지가 없으면 기본 SVG 유지
    },
    error: function () {
      // 에러 발생 시 기본 SVG 유지
      console.log("프로필 정보를 불러올 수 없습니다.");
    }
  });
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

        // 서브메뉴인 경우 상위 메뉴 활성화 및 자동 확장
        let parentMenu = parentLi.closest(".sub-menu");
        if (parentMenu) {
          // 상위 메뉴의 <li> 찾기
          let topMenuLi = parentMenu.previousElementSibling;
          if (topMenuLi && topMenuLi.classList.contains("iocn-link")) {
            // iocn-link의 부모 li 찾기
            let topMenu = topMenuLi.closest("li");
            if (topMenu) {
              topMenu.classList.add("active", "showMenu"); // 최상위 <li>에 active와 showMenu 추가
            }
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

let contentAreaClickHandler = function () {
  $("#content-area").click(function () {
    let sideBarStatus = JSON.parse(localStorage.getItem("sidebarStatus"));
    // 사이드바가 펼쳐진 상태(true)일 때만 접기
    if (sideBarStatus === true) {
      localStorage.setItem("sidebarStatus", JSON.stringify(false));
      applySideBarStatus(false);
    }
  });
};

let onClickProfileContent = function () {
  $(".profile-content").click(function () {
    // 프로필 모달 열기
    const profileModal = new bootstrap.Modal(document.getElementById('profileModal'));
    profileModal.show();
  });
};