$(document).ready(function() {
    var errorMessage = getParameterByName('error');
    if (errorMessage) {
        alert(errorMessage);
    }
});

// URL 파라미터를 가져오는 함수
function getParameterByName(name) {
    var url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

