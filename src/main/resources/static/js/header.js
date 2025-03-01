$(document).ready(function () {
  $(".arrow").click(function () {
    $(this).parent().parent().toggleClass("showMenu");
  });

  $(".bx-menu").click(function () {
    $(".sidebar").toggleClass("close");
  });
});