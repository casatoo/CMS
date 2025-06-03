let initDateModal = function () {
    $('#dateModal').on('shown.bs.modal', function () {
        $('#startTime').focus();
    });
    $('#dateModal').on('hide.bs.modal', function () {
        document.activeElement.blur(); // aria-hidden 포커싱 문제
        $('#startTime').val('');
        $('#endTime').val('');
    });
}
