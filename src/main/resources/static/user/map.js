// 지도 초기화 함수
function initMap() {
    // 사용자의 현재 위치 가져오기
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, showError);
    } else {
        defaultPosition()
    }
}

// 위치 가져오기 성공 함수
function showPosition(position) {
    var lat = position.coords.latitude;
    var lng = position.coords.longitude;

    var map = new naver.maps.Map('map', {
            center: new naver.maps.LatLng(lat, lng),
            zoom: 15,
            maxZoom: 18,
            minZoom: 13,
            zoomControl: true,
            zoomControlOptions: {
                position: naver.maps.Position.TOP_RIGHT
            }
        });

    // 현재 위치 마커 추가
    var marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(lat, lng),
        map: map
    });
}

// 기본 위치
function defaultPosition() {
    var lat = 37.3595704
    var lng = 127.105399

    var map = new naver.maps.Map('map', {
            center: new naver.maps.LatLng(lat, lng),
            zoom: 15,
            maxZoom: 18,
            minZoom: 13,
            zoomControl: true,
            zoomControlOptions: {
                position: naver.maps.Position.TOP_RIGHT
            }
        });

    var marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(lat, lng),
        map: map
    });
}

// 위치 가져오기 실패 콜백 함수
function showError(error) {
    switch(error.code) {
        case error.PERMISSION_DENIED:
            defaultPosition()
            break;
        case error.POSITION_UNAVAILABLE:
            alert("Location information is unavailable.");
            break;
        case error.TIMEOUT:
            alert("The request to get user location timed out.");
            break;
        case error.UNKNOWN_ERROR:
            alert("An unknown error occurred.");
            break;
    }
}
