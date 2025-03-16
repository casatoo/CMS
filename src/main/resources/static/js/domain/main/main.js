let apiUri = "/api/v1/login-history";

$(document).ready(function () {
  onClickTestBtn();
});

let onClickTestBtn = function () {
  $("#testBtn").click(function () {
    $.get(apiUri + "/user-id", {userId: '73afdc27-020a-11f0-80db-18c04d93a2b9'}, function (res) {
      if (res.status === 200) {
        console.log(res.payload);
      }
    });
  });
}